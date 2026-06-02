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

    // -----------------------------------------------------------
    // CRIAR AVALIAÇÃO
    // Regras:
    //   1. Solicitação deve existir
    //   2. Solicitação deve estar FINALIZADA
    //   3. Solicitação não pode já ter avaliação
    // -----------------------------------------------------------
    @Transactional
    public AvaliacaoResponseDTO criar(AvaliacaoRequestDTO dto) {

        // Busca a solicitação (reutiliza método do SolicitacaoService)
        Solicitacao solicitacao = solicitacaoService
                .buscarEntidadePorId(dto.getSolicitacaoId());

        // Regra 1: só avalia serviço finalizado
        // Sem essa regra, o cliente poderia avaliar antes de receber o serviço
        if (solicitacao.getStatus() != StatusSolicitacao.finalizado) {
            throw new RegraNegocioException(
                    "Só é possível avaliar solicitações finalizadas. " +
                            "Status atual: " + solicitacao.getStatus().name()
            );
        }

        // Regra 2: cada solicitação só pode ter uma avaliação
        // Verificamos antes de tentar salvar para dar mensagem clara
        // (sem isso, o banco lançaria uma exceção genérica de UNIQUE violation)
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

    // -----------------------------------------------------------
    // BUSCAR POR ID
    // -----------------------------------------------------------
    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorId(Integer id) {
        return avaliacaoRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Avaliação não encontrada com ID: " + id));
    }

    // -----------------------------------------------------------
    // BUSCAR POR SOLICITAÇÃO
    // Útil para o Flutter checar se uma solicitação já foi avaliada
    // -----------------------------------------------------------
    @Transactional(readOnly = true)
    public AvaliacaoResponseDTO buscarPorSolicitacao(Integer solicitacaoId) {
        return avaliacaoRepository.findBySolicitacaoId(solicitacaoId)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Nenhuma avaliação encontrada para a solicitação ID: "
                                + solicitacaoId));
    }

    // -----------------------------------------------------------
    // LISTAR POR PRESTADOR
    // Exibe todas as avaliações recebidas no perfil do prestador
    // -----------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AvaliacaoResponseDTO> listarPorPrestador(Integer prestadorId) {
        return avaliacaoRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------------
    // MÉDIA DO PRESTADOR
    // Exibida no card do prestador na home do cliente
    // -----------------------------------------------------------
    @Transactional(readOnly = true)
    public MediaAvaliacaoResponseDTO calcularMedia(Integer prestadorId) {

        // Valida que o prestador existe
        var prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new RegraNegocioException(
                        "Prestador não encontrado com ID: " + prestadorId));

        Double media = avaliacaoRepository
                .calcularMediaPorPrestador(prestadorId);

        List<AvaliacaoResponseDTO> avaliacoes =
                listarPorPrestador(prestadorId);

        // Se não tem nenhuma avaliação ainda, média retorna null do banco
        // Tratamos como 0.0 para não quebrar o Flutter
        return new MediaAvaliacaoResponseDTO(
                prestadorId,
                prestador.getNome(),
                media != null ? Math.round(media * 10.0) / 10.0 : 0.0,
                // Math.round arredonda para 1 casa decimal: 4.666... → 4.7
                avaliacoes.size()
        );
    }

    // -----------------------------------------------------------
    // CONVERTER Entity → DTO
    // -----------------------------------------------------------
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