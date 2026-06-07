package project.bicos.api.repository;

import project.bicos.api.models.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {

    Optional<Prestador> findByEmail(String email);

    Optional<Prestador> findByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    @Query("SELECT DISTINCT a.prestador FROM Anuncio a WHERE a.categoria.nome = :nomeCategoria")
    List<Prestador> findByCategoriaNome(@Param("nomeCategoria") String nomeCategoria);
}
