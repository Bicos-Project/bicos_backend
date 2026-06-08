package project.bicos.api.repository;

import project.bicos.api.models.Solicitacao;
import project.bicos.api.models.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {

    List<Solicitacao> findByClienteId(Integer clienteId);

    boolean existsByClienteIdAndPrestadorIdAndStatusNot(
            Integer clienteId,
            Integer prestadorId,
            StatusSolicitacao status
    );

    @Query("SELECT s FROM Solicitacao s " +
            "WHERE s.prestador.id = :prestadorId")
    List<Solicitacao> findByPrestadorId(
            @Param("prestadorId") Integer prestadorId);
}