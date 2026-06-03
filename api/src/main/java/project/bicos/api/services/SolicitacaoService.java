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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ClienteRepository clienteRepository;
    private final AnuncioRepository anuncioRepository;

    @Transactional
    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO dto) {

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Cliente não encontrado com ID: " + dto.getClienteId()));

        Anuncio anuncio = anuncioRepository.findById(dto.getAnuncioId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + dto.getAnuncioId()));

        if (anuncio.getStatus().name().equals("inativo")) {
            throw new RegraNegocioException(
                    "Este anúncio não está mais disponível.");
        }

        boolean temSolicitacaoAberta = solicitacaoRepository
                .existsByClienteIdAndAnuncioIdAndStatusNot(
                        dto.getClienteId(),
                        dto.getAnuncioId(),
                        StatusSolicitacao.finalizado
                );

        if (temSolicitacaoAberta) {
            throw new RegraNegocioException(
                    "Você já possui uma solicitação em andamento para este serviço.");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setDataSolicitacao(
                dto.getDataSolicitacao() != null
                        ? dto.getDataSolicitacao()
                        : LocalDate.now()
        );
        solicitacao.setStatus(StatusSolicitacao.orcamento);
        solicitacao.setCliente(cliente);
        solicitacao.setAnuncio(anuncio);

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
            case esperando_pagamento  -> StatusSolicitacao.finalizado;
            case finalizado           -> throw new RegraNegocioException(
                    "Esta solicitação já foi finalizada.");
        };

        solicitacao.setStatus(proximoStatus);
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
    public List<SolicitacaoResponseDTO> listarPorAnuncio(Integer anuncioId) {
        return solicitacaoRepository.findByAnuncioId(anuncioId)
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
        return new SolicitacaoResponseDTO(
                s.getId(),
                s.getDescricao(),
                s.getDataSolicitacao(),
                s.getStatus().name(),
                s.getCliente().getId(),
                s.getCliente().getNome(),
                s.getAnuncio().getId(),
                s.getAnuncio().getTitulo(),
                s.getAnuncio().getPrestador().getId(),
                s.getAnuncio().getPrestador().getNome()
        );
    }
}