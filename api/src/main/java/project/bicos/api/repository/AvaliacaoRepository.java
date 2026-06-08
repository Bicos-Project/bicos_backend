package project.bicos.api.repository;

import project.bicos.api.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    boolean existsBySolicitacaoIdAndAvaliadorTipo(Integer solicitacaoId, String avaliadorTipo);

    Optional<Avaliacao> findBySolicitacaoIdAndAvaliadorTipo(Integer solicitacaoId, String avaliadorTipo);

    List<Avaliacao> findBySolicitacaoId(Integer solicitacaoId);

    @Query("SELECT a FROM Avaliacao a " +
            "WHERE a.solicitacao.prestador.id = :prestadorId")
    List<Avaliacao> findByPrestadorId(@Param("prestadorId") Integer prestadorId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a " +
            "WHERE a.solicitacao.prestador.id = :prestadorId")
    Double calcularMediaPorPrestador(@Param("prestadorId") Integer prestadorId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a " +
            "WHERE a.solicitacao.cliente.id = :clienteId " +
            "AND a.avaliadorTipo = 'PRESTADOR'")
    Double calcularMediaCliente(@Param("clienteId") Integer clienteId);
}
