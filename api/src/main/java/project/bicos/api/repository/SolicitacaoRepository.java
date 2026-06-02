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

    List<Solicitacao> findByAnuncioId(Integer anuncioId);

    // Usado na regra de negócio: cliente não pode ter duas solicitações
    // abertas para o mesmo anúncio
    boolean existsByClienteIdAndAnuncioIdAndStatusNot(
            Integer clienteId,
            Integer anuncioId,
            StatusSolicitacao status
    );

    @Query("SELECT s FROM Solicitacao s " +
            "WHERE s.anuncio.prestador.id = :prestadorId")
    List<Solicitacao> findByPrestadorId(
            @Param("prestadorId") Integer prestadorId);
}