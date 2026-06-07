package project.bicos.api.repository;

import project.bicos.api.models.AnuncioFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnuncioFotoRepository extends JpaRepository<AnuncioFoto, Integer> {

    List<AnuncioFoto> findByAnuncioIdOrderByOrdemAsc(Integer anuncioId);

    void deleteByAnuncioId(Integer anuncioId);
}
