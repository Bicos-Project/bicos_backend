package project.bicos.api.repository;

import project.bicos.api.models.Favorito;
import project.bicos.api.models.FavoritoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, FavoritoId> {
    List<Favorito> findByIdClienteId(Integer clienteId);
    boolean existsByIdClienteIdAndIdPrestadorId(Integer clienteId, Integer prestadorId);
    void deleteByIdClienteIdAndIdPrestadorId(Integer clienteId, Integer prestadorId);
}
