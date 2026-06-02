package project.bicos.api.repository;

import project.bicos.api.models.Anuncio;
import project.bicos.api.models.enums.StatusAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {

    List<Anuncio> findByPrestadorId(Integer prestadorId);

    List<Anuncio> findByCategoriaIdAndStatus(Integer categoriaId, StatusAnuncio status);

    List<Anuncio> findByStatus(StatusAnuncio status);
}