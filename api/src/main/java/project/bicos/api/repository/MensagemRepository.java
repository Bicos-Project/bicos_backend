package project.bicos.api.repository;

import project.bicos.api.models.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Integer> {
    List<Mensagem> findBySolicitacaoIdOrderByDataHoraAsc(Integer solicitacaoId);
}
