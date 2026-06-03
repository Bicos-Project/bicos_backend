package project.bicos.api.services;

import project.bicos.api.dto.categoria.CategoriaRequestDTO;
import project.bicos.api.dto.categoria.CategoriaResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Categoria;
import project.bicos.api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {

        if (categoriaRepository.existsByNome(dto.getNome())) {
            throw new RegraNegocioException("Já existe uma categoria com esse nome.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        return toResponseDTO(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RegraNegocioException(
                        "Categoria não encontrada com ID: " + id));
    }

    Categoria buscarEntidadePorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException(
                        "Categoria não encontrada com ID: " + id));
    }

    private CategoriaResponseDTO toResponseDTO(Categoria c) {
        return new CategoriaResponseDTO(c.getId(), c.getNome(), c.getDescricao());
    }
}
