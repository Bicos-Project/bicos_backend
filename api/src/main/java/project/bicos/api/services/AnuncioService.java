package project.bicos.api.services;

import project.bicos.api.dto.anuncio.AnuncioRequestDTO;
import project.bicos.api.dto.anuncio.AnuncioResponseDTO;
import project.bicos.api.dto.categoria.CategoriaResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Anuncio;
import project.bicos.api.models.Categoria;
import project.bicos.api.models.Prestador;
import project.bicos.api.models.enums.StatusAnuncio;
import project.bicos.api.repository.AnuncioRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final PrestadorRepository prestadorRepository;
    private final CategoriaService categoriaService;

    // ---------------------------------------------------------------
    // CRIAR ANÚNCIO
    // ---------------------------------------------------------------
    @Transactional
    public AnuncioResponseDTO criar(AnuncioRequestDTO dto) {

        // Valida e busca o prestador
        Prestador prestador = prestadorRepository.findById(dto.getPrestadorId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Prestador não encontrado com ID: " + dto.getPrestadorId()));

        // Valida e busca a categoria (reutiliza método do CategoriaService)
        Categoria categoria = categoriaService.buscarEntidadePorId(dto.getCategoriaId());

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(dto.getTitulo());
        anuncio.setDescricao(dto.getDescricao());
        anuncio.setValorBase(dto.getValorBase());
        anuncio.setStatus(StatusAnuncio.ativo); // todo anúncio começa ativo
        anuncio.setPrestador(prestador);
        anuncio.setCategoria(categoria);

        return toResponseDTO(anuncioRepository.save(anuncio));
    }

    // ---------------------------------------------------------------
    // LISTAR TODOS OS ATIVOS — home do cliente
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarAtivos() {
        return anuncioRepository.findByStatus(StatusAnuncio.ativo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // LISTAR POR CATEGORIA — quando cliente clica numa categoria
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarPorCategoria(Integer categoriaId) {
        return anuncioRepository
                .findByCategoriaIdAndStatus(categoriaId, StatusAnuncio.ativo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // LISTAR POR PRESTADOR — painel do prestador
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarPorPrestador(Integer prestadorId) {
        return anuncioRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------------
    // BUSCAR POR ID
    // ---------------------------------------------------------------
    @Transactional(readOnly = true)
    public AnuncioResponseDTO buscarPorId(Integer id) {
        return anuncioRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + id));
    }

    // ---------------------------------------------------------------
    // ATUALIZAR
    // ---------------------------------------------------------------
    @Transactional
    public AnuncioResponseDTO atualizar(Integer id, AnuncioRequestDTO dto) {
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + id));

        Categoria categoria = categoriaService.buscarEntidadePorId(dto.getCategoriaId());

        anuncio.setTitulo(dto.getTitulo());
        anuncio.setDescricao(dto.getDescricao());
        anuncio.setValorBase(dto.getValorBase());
        anuncio.setCategoria(categoria);

        // Dirty checking cuida do UPDATE automaticamente
        return toResponseDTO(anuncio);
    }

    // ---------------------------------------------------------------
    // DESATIVAR — soft delete: não apaga, só muda status
    // ---------------------------------------------------------------
    @Transactional
    public AnuncioResponseDTO desativar(Integer id) {
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + id));

        // Regra: só desativa se estiver ativo
        if (anuncio.getStatus() == StatusAnuncio.inativo) {
            throw new RegraNegocioException("Anúncio já está inativo.");
        }

        anuncio.setStatus(StatusAnuncio.inativo);
        return toResponseDTO(anuncio);
    }

    // ---------------------------------------------------------------
    // CONVERTER Entity → DTO
    // ---------------------------------------------------------------
    private AnuncioResponseDTO toResponseDTO(Anuncio a) {
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                a.getCategoria().getId(),
                a.getCategoria().getNome(),
                a.getCategoria().getDescricao()
        );

        return new AnuncioResponseDTO(
                a.getId(),
                a.getTitulo(),
                a.getDescricao(),
                a.getValorBase(),
                a.getStatus().name(),
                categoriaDTO,
                a.getPrestador().getId(),
                a.getPrestador().getNome()
        );
    }
}