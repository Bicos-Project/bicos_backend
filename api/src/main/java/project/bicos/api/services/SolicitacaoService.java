package project.bicos.api.services;

import project.bicos.api.dto.solicitacao.SolicitacaoRequestDTO;
import project.bicos.api.dto.solicitacao.SolicitacaoResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.*;
import project.bicos.api.models.enums.StatusSolicitacao;
import project.bicos.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;
    private final AvaliacaoRepository avaliacaoRepository;

    @Transactional
    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Cliente não encontrado com ID: " + dto.getClienteId()));

        Prestador prestador = prestadorRepository.findById(dto.getPrestadorId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Prestador não encontrado com ID: " + dto.getPrestadorId()));

        boolean temSolicitacaoAberta = solicitacaoRepository
                .existsByClienteIdAndPrestadorIdAndStatusNot(
                        dto.getClienteId(),
                        dto.getPrestadorId(),
                        StatusSolicitacao.finalizado
                );

        if (temSolicitacaoAberta) {
            throw new RegraNegocioException(
                    "Você já possui uma solicitação em andamento para este prestador.");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setDataSolicitacao(
                dto.getDataSolicitacao() != null
                        ? dto.getDataSolicitacao()
                        : LocalDate.now()
        );
        solicitacao.setDataEstimada(dto.getDataEstimada());
        solicitacao.setValorSugerido(dto.getValorSugerido());
        solicitacao.setStatus(StatusSolicitacao.orcamento);
        solicitacao.setPrestadorConfirmouPagamento(false);
        solicitacao.setClienteConfirmouPagamento(false);
        solicitacao.setCliente(cliente);
        solicitacao.setPrestador(prestador);

        return toResponseDTO(solicitacaoRepository.save(solicitacao));
    }

    @Transactional
    public SolicitacaoResponseDTO avancarStatus(Integer id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Solicitação não encontrada com ID: " + id));

        StatusSolicitacao statusAtual = solicitacao.getStatus();

        StatusSolicitacao proximoStatus = switch (statusAtual) {
            case orcamento            -> StatusSolicitacao.em_andamento;
            case em_andamento         -> StatusSolicitacao.esperando_pagamento;
            case esperando_pagamento  -> throw new RegraNegocioException(
                    "Pagamento requer confirmação de ambas as partes. Use o endpoint de confirmação.");
            case finalizado           -> throw new RegraNegocioException(
                    "Esta solicitação já foi finalizada.");
            case cancelado            -> throw new RegraNegocioException(
                    "Esta solicitação foi cancelada.");
        };

        solicitacao.setStatus(proximoStatus);
        return toResponseDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResponseDTO confirmarPagamento(Integer id, String tipo) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Solicitação não encontrada com ID: " + id));

        if (solicitacao.getStatus() != StatusSolicitacao.esperando_pagamento) {
            throw new RegraNegocioException(
                    "Só é possível confirmar pagamento quando o status é 'esperando_pagamento'. " +
                    "Status atual: " + solicitacao.getStatus().name());
        }

        if ("PRESTADOR".equals(tipo)) {
            if (Boolean.TRUE.equals(solicitacao.getPrestadorConfirmouPagamento())) {
                throw new RegraNegocioException("Prestador já confirmou o pagamento.");
            }
            solicitacao.setPrestadorConfirmouPagamento(true);
        } else if ("CLIENTE".equals(tipo)) {
            if (Boolean.TRUE.equals(solicitacao.getClienteConfirmouPagamento())) {
                throw new RegraNegocioException("Cliente já confirmou o pagamento.");
            }
            solicitacao.setClienteConfirmouPagamento(true);
        } else {
            throw new RegraNegocioException("Tipo inválido. Use 'PRESTADOR' ou 'CLIENTE'.");
        }

        if (Boolean.TRUE.equals(solicitacao.getPrestadorConfirmouPagamento())
                && Boolean.TRUE.equals(solicitacao.getClienteConfirmouPagamento())) {
            solicitacao.setStatus(StatusSolicitacao.finalizado);
        }

        return toResponseDTO(solicitacao);
    }

    @Transactional
    public SolicitacaoResponseDTO recusar(Integer id) {
        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Solicitação não encontrada com ID: " + id));
        solicitacao.setStatus(StatusSolicitacao.cancelado);
        return toResponseDTO(solicitacao);
    }

    @Transactional(readOnly = true)
    public SolicitacaoResponseDTO buscarPorId(Integer id) {
        return solicitacaoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Solicitação não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> listarPorCliente(Integer clienteId) {
        return solicitacaoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDTO> listarPorPrestador(Integer prestadorId) {
        return solicitacaoRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Solicitacao buscarEntidadePorId(Integer id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Solicitação não encontrada com ID: " + id));
    }

    private SolicitacaoResponseDTO toResponseDTO(Solicitacao s) {
        boolean clienteAvaliou = avaliacaoRepository
                .existsBySolicitacaoIdAndAvaliadorTipo(s.getId(), "CLIENTE");
        boolean prestadorAvaliou = avaliacaoRepository
                .existsBySolicitacaoIdAndAvaliadorTipo(s.getId(), "PRESTADOR");

        Double clienteAvaliacaoMedia = avaliacaoRepository
                .calcularMediaCliente(s.getCliente().getId());

        String categoriaNome = s.getPrestador().getCategoria() != null
                ? s.getPrestador().getCategoria().getNome()
                : null;

        return new SolicitacaoResponseDTO(
                s.getId(),
                s.getDescricao(),
                s.getDataSolicitacao(),
                s.getDataEstimada(),
                s.getValorSugerido(),
                s.getStatus().name(),
                s.getCliente().getId(),
                s.getCliente().getNome(),
                clienteAvaliacaoMedia != null
                        ? Math.round(clienteAvaliacaoMedia * 10.0) / 10.0
                        : 0.0,
                s.getPrestador().getId(),
                s.getPrestador().getNome(),
                categoriaNome,
                s.getPrestadorConfirmouPagamento(),
                s.getClienteConfirmouPagamento(),
                clienteAvaliou,
                prestadorAvaliou
        );
    }
}