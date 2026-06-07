package project.bicos.api.repository;

import project.bicos.api.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    boolean existsByNome(String nome);

    Optional<Categoria> findByNome(String nome);
}