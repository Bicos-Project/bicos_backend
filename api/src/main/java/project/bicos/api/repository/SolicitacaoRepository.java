package project.bicos.api.repository;

import project.bicos.api.models.Solicitacao;
import project.bicos.api.models.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {

    List<Solicitacao> findByClienteId(Integer clienteId);

    boolean existsByClienteIdAndPrestadorIdAndStatusNotIn(
            Integer clienteId,
            Integer prestadorId,
            List<StatusSolicitacao> statuses
    );

    @Query("SELECT s FROM Solicitacao s " +
            "WHERE s.prestador.id = :prestadorId")
    List<Solicitacao> findByPrestadorId(
            @Param("prestadorId") Integer prestadorId);
}