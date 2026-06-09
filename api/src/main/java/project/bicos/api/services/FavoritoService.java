package project.bicos.api.services;

import project.bicos.api.dto.prestador.PrestadorResponseDTO;
import project.bicos.api.models.Favorito;
import project.bicos.api.models.FavoritoId;
import project.bicos.api.models.Prestador;
import project.bicos.api.repository.FavoritoRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final PrestadorRepository prestadorRepository;
    private final PrestadorService prestadorService;

    @Transactional(readOnly = true)
    public List<PrestadorResponseDTO> listarPorCliente(Integer clienteId) {
        List<Favorito> favoritos = favoritoRepository.findByIdClienteId(clienteId);
        List<Integer> prestadorIds = favoritos.stream()
                .map(f -> f.getId().getPrestadorId())
                .collect(Collectors.toList());
        return prestadorRepository.findAllById(prestadorIds).stream()
                .map(prestadorService::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Integer> listarIdsPorCliente(Integer clienteId) {
        return favoritoRepository.findByIdClienteId(clienteId).stream()
                .map(f -> f.getId().getPrestadorId())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isFavorito(Integer clienteId, Integer prestadorId) {
        return favoritoRepository.existsByIdClienteIdAndIdPrestadorId(clienteId, prestadorId);
    }

    @Transactional
    public void adicionar(Integer clienteId, Integer prestadorId) {
        if (!isFavorito(clienteId, prestadorId)) {
            FavoritoId id = new FavoritoId(clienteId, prestadorId);
            favoritoRepository.save(new Favorito(id));
        }
    }

    @Transactional
    public void remover(Integer clienteId, Integer prestadorId) {
        favoritoRepository.deleteByIdClienteIdAndIdPrestadorId(clienteId, prestadorId);
    }

    @Transactional
    public boolean toggle(Integer clienteId, Integer prestadorId) {
        if (isFavorito(clienteId, prestadorId)) {
            remover(clienteId, prestadorId);
            return false;
        } else {
            adicionar(clienteId, prestadorId);
            return true;
        }
    }
}
