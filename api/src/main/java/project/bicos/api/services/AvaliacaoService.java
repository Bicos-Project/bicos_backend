package project.bicos.api.services;

import project.bicos.api.dto.avaliacao.AvaliacaoRequestDTO;
import project.bicos.api.dto.avaliacao.AvaliacaoResponseDTO;
import project.bicos.api.dto.avaliacao.MediaAvaliacaoResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Avaliacao;
import project.bicos.api.models.Cliente;
import project.bicos.api.models.Prestador;
import project.bicos.api.models.Solicitacao;
import project.bicos.api.models.enums.StatusSolicitacao;
import project.bicos.api.repository.AvaliacaoRepository;
import project.bicos.api.repository.ClienteRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final SolicitacaoService solicitacaoService;
    private final PrestadorRepository prestadorRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoRequestDTO dto) {

        Solicitacao solicitacao = solicitacaoService
                .buscarEntidadePorId(dto.getSolicitacaoId());

        if (solicitacao.getStatus() != StatusSolicitacao.finalizado) {
            throw new RegraNegocioException(
                    "Só é possível avaliar solicitações finalizadas. " +
                            "Status atual: " + solicitacao.getStatus().name()
            );
        }

        if (avaliacaoRepository.existsBySolicitacaoIdAndAvaliadorTipo(
                dto.getSolicitacaoId(), dto.getAvaliadorTipo())) {
            throw new RegraNegocioException(
                    "Você já avaliou esta solicitação."
            );
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setAvaliadorTipo(dto.getAvaliadorTipo());
        avaliacao.setSolicitacao(solicitacao);

        Avaliacao saved = avaliacaoRepository.save(avaliacao);

        // Recalcular e atualizar a média do prestador
        Prestador prestador = solicitacao.getPrestador();
        Double media = avaliacaoRepository
                .calcularMediaPorPrestador(prestador.getId());
        prestador.setAvaliacao(media != null
                ? java.math.BigDecimal.valueOf(
                        Math.round(media * 10.0) / 10.0)
                : java.math.BigDecimal.ZERO);
        prestadorRepository.save(prestador);

        // Recalcular e atualizar a média do cliente (quando avaliado por prestador)
        if ("PRESTADOR".equals(dto.getAvaliadorTipo())) {
            Cliente cliente = solicitacao.getCliente();
            Double mediaCliente = avaliacaoRepository
                    .calcularMediaCliente(cliente.getId());
            cliente.setAvaliacao(mediaCliente != null
                    ? java.math.BigDecimal.valueOf(
                            Math.round(mediaCliente * 10.0) / 10.0)
                    : java.math.BigDecimal.ZERO);
            clienteRepository.save(cliente);
        }

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorId(Integer id) {
        return avaliacaoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Avaliação não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> buscarPorSolicitacao(Integer solicitacaoId) {
        return avaliacaoRepository.findBySolicitacaoId(solicitacaoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorPrestador(Integer prestadorId) {
        return avaliacaoRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MediaAvaliacaoResponseDTO calcularMedia(Integer prestadorId) {

        var prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new RegraNegocioException(
                        "Prestador não encontrado com ID: " + prestadorId));

        Double media = avaliacaoRepository
                .calcularMediaPorPrestador(prestadorId);

        List<AvaliacaoResponseDTO> avaliacoes =
                listarPorPrestador(prestadorId);

        return new MediaAvaliacaoResponseDTO(
                prestadorId,
                prestador.getNome(),
                media != null ? Math.round(media * 10.0) / 10.0 : 0.0,
                avaliacoes.size()
        );
    }

    private AvaliacaoResponseDTO toResponseDTO(Avaliacao a) {
        Solicitacao s = a.getSolicitacao();

        return new AvaliacaoResponseDTO(
                a.getId(),
                a.getNota(),
                a.getComentario(),
                a.getAvaliadorTipo(),
                s.getId(),
                s.getPrestador().getId(),
                s.getPrestador().getNome(),
                s.getCliente().getId(),
                s.getCliente().getNome()
        );
    }
}