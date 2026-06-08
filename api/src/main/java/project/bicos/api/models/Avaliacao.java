package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacao", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_solicitacao", "avaliador_tipo"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avaliacao_seq")
    @SequenceGenerator(
            name = "avaliacao_seq",
            sequenceName = "avaliacao_id_avaliacao_seq",
            allocationSize = 1
    )
    @Column(name = "id_avaliacao")
    private Integer id;

    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "avaliador_tipo", nullable = false, length = 10)
    private String avaliadorTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitacao", nullable = false)
    private Solicitacao solicitacao;
}
