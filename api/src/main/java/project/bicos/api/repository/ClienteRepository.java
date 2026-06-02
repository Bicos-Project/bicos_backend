package project.bicos.api.repository;

import project.bicos.api.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Spring traduz isso para: SELECT * FROM cliente WHERE email = ?
    Optional<Cliente> findByEmail(String email);

    // SELECT * FROM cliente WHERE cpf = ?
    Optional<Cliente> findByCpf(String cpf);

    // SELECT COUNT(*) > 0 FROM cliente WHERE email = ?
    // Útil para validar duplicidade antes de cadastrar
    boolean existsByEmail(String email);

    // SELECT COUNT(*) > 0 FROM cliente WHERE cpf = ?
    boolean existsByCpf(String cpf);
}
