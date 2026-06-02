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

    // Verifica se já existe avaliação para essa solicitação
    // Usado antes de tentar salvar para dar erro claro ao invés de exceção do banco
    boolean existsBySolicitacaoId(Integer solicitacaoId);

    // Busca a avaliação de uma solicitação específica
    Optional<Avaliacao> findBySolicitacaoId(Integer solicitacaoId);

    // Todas as avaliações recebidas por um prestador
    // Caminho: Avaliacao → Solicitacao → Anuncio → Prestador
    @Query("SELECT a FROM Avaliacao a " +
            "WHERE a.solicitacao.anuncio.prestador.id = :prestadorId")
    List<Avaliacao> findByPrestadorId(@Param("prestadorId") Integer prestadorId);

    // Média das notas de um prestador — útil para exibir no perfil
    @Query("SELECT AVG(a.nota) FROM Avaliacao a " +
            "WHERE a.solicitacao.anuncio.prestador.id = :prestadorId")
    Double calcularMediaPorPrestador(@Param("prestadorId") Integer prestadorId);
}
