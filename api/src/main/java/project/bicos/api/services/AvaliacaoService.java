package project.bicos.api.services;

import project.bicos.api.dto.avaliacao.AvaliacaoRequestDTO;
import project.bicos.api.dto.avaliacao.AvaliacaoResponseDTO;
import project.bicos.api.dto.avaliacao.MediaAvaliacaoResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Avaliacao;
import project.bicos.api.models.Solicitacao;
import project.bicos.api.models.enums.StatusSolicitacao;
import project.bicos.api.repository.AvaliacaoRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final SolicitacaoService solicitacaoService;
    private final PrestadorRepository prestadorRepository;

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

        if (avaliacaoRepository.existsBySolicitacaoId(dto.getSolicitacaoId())) {
            throw new RegraNegocioException(
                    "Essa solicitação já possui uma avaliação."
            );
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setNota(dto.getNota());
        avaliacao.setComentario(dto.getComentario());
        avaliacao.setSolicitacao(solicitacao);

        return toResponseDTO(avaliacaoRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorId(Integer id) {
        return avaliacaoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Avaliação não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorSolicitacao(Integer solicitacaoId) {
        return avaliacaoRepository.findBySolicitacaoId(solicitacaoId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Nenhuma avaliação encontrada para a solicitação ID: "
                                + solicitacaoId));
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
                s.getId(),
                s.getAnuncio().getPrestador().getId(),
                s.getAnuncio().getPrestador().getNome(),
                s.getCliente().getId(),
                s.getCliente().getNome()
        );
    }
}