package project.bicos.api.repository;

import project.bicos.api.models.PrestadorFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestadorFotoRepository extends JpaRepository<PrestadorFoto, Integer> {

    List<PrestadorFoto> findByPrestadorIdOrderByOrdemAsc(Integer prestadorId);

    void deleteByPrestadorId(Integer prestadorId);
}
