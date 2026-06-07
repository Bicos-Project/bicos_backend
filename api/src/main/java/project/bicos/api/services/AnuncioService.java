package project.bicos.api.services;

import project.bicos.api.dto.anuncio.AnuncioRequestDTO;
import project.bicos.api.dto.anuncio.AnuncioResponseDTO;
import project.bicos.api.dto.categoria.CategoriaResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Anuncio;
import project.bicos.api.models.AnuncioFoto;
import project.bicos.api.models.Categoria;
import project.bicos.api.models.Prestador;
import project.bicos.api.models.enums.StatusAnuncio;
import project.bicos.api.repository.AnuncioFotoRepository;
import project.bicos.api.repository.AnuncioRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final AnuncioRepository anuncioRepository;
    private final PrestadorRepository prestadorRepository;
    private final CategoriaService categoriaService;
    private final AnuncioFotoRepository anuncioFotoRepository;
    private final StorageService storageService;

    @Transactional
    public AnuncioResponseDTO criar(AnuncioRequestDTO dto) {

        Prestador prestador = prestadorRepository.findById(dto.getPrestadorId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Prestador não encontrado com ID: " + dto.getPrestadorId()));

        Categoria categoria = categoriaService.buscarEntidadePorId(dto.getCategoriaId());

        Anuncio anuncio = new Anuncio();
        anuncio.setTitulo(dto.getTitulo());
        anuncio.setDescricao(dto.getDescricao());
        anuncio.setValorBase(dto.getValorBase());
        anuncio.setStatus(StatusAnuncio.ativo);
        anuncio.setPrestador(prestador);
        anuncio.setCategoria(categoria);

        return toResponseDTO(anuncioRepository.save(anuncio));
    }

    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarAtivos() {
        return anuncioRepository.findByStatus(StatusAnuncio.ativo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarPorCategoria(Integer categoriaId) {
        return anuncioRepository
                .findByCategoriaIdAndStatus(categoriaId, StatusAnuncio.ativo)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AnuncioResponseDTO> listarPorPrestador(Integer prestadorId) {
        return anuncioRepository.findByPrestadorId(prestadorId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AnuncioResponseDTO buscarPorId(Integer id) {
        return anuncioRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + id));
    }

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

        return toResponseDTO(anuncio);
    }

    @Transactional
    public AnuncioResponseDTO desativar(Integer id) {
        Anuncio anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + id));

        if (anuncio.getStatus() == StatusAnuncio.inativo) {
            throw new RegraNegocioException("Anúncio já está inativo.");
        }

        anuncio.setStatus(StatusAnuncio.inativo);
        return toResponseDTO(anuncio);
    }

    @Transactional
    public AnuncioResponseDTO adicionarFoto(Integer anuncioId, MultipartFile file) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + anuncioId));

        String url = storageService.salvar(file);

        int nextOrdem = anuncio.getFotos().isEmpty() ? 0 :
                anuncio.getFotos().stream().mapToInt(AnuncioFoto::getOrdem).max().orElse(0) + 1;

        AnuncioFoto foto = new AnuncioFoto();
        foto.setUrl(url);
        foto.setOrdem(nextOrdem);
        foto.setAnuncio(anuncio);

        anuncio.getFotos().add(foto);
        return toResponseDTO(anuncioRepository.save(anuncio));
    }

    @Transactional
    public AnuncioResponseDTO removerFoto(Integer anuncioId, Integer fotoId) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RegraNegocioException(
                        "Anúncio não encontrado com ID: " + anuncioId));

        AnuncioFoto foto = anuncioFotoRepository.findById(fotoId)
                .orElseThrow(() -> new RegraNegocioException(
                        "Foto não encontrada com ID: " + fotoId));

        if (!foto.getAnuncio().getId().equals(anuncioId)) {
            throw new RegraNegocioException("Foto não pertence a este anúncio.");
        }

        storageService.deletar(foto.getUrl());
        anuncio.getFotos().remove(foto);
        anuncioFotoRepository.delete(foto);

        return toResponseDTO(anuncio);
    }

    private AnuncioResponseDTO toResponseDTO(Anuncio a) {
        CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO(
                a.getCategoria().getId(),
                a.getCategoria().getNome(),
                a.getCategoria().getDescricao()
        );

        List<String> fotosUrls = a.getFotos() != null
                ? a.getFotos().stream().map(AnuncioFoto::getUrl).collect(Collectors.toList())
                : new ArrayList<>();

        return new AnuncioResponseDTO(
                a.getId(),
                a.getTitulo(),
                a.getDescricao(),
                a.getValorBase(),
                a.getStatus().name(),
                categoriaDTO,
                a.getPrestador().getId(),
                a.getPrestador().getNome(),
                fotosUrls
        );
    }
}