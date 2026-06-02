package project.bicos.api.repository;

import project.bicos.api.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Verifica se já existe categoria com esse nome (para evitar duplicatas)
    boolean existsByNome(String nome);
}