package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacao")
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

    // Nota de 1 a 5 — validada no DTO e reforçada aqui
    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    // @OneToOne porque o UNIQUE no banco garante 1 avaliação por solicitação
    // Não usamos cascade aqui: deletar avaliação não deve deletar a solicitação
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitacao", nullable = false, unique = true)
    private Solicitacao solicitacao;
}
