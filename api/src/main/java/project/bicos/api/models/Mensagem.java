package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensagem_seq")
    @SequenceGenerator(
            name = "mensagem_seq",
            sequenceName = "mensagem_id_mensagem_seq",
            allocationSize = 1
    )
    @Column(name = "id_mensagem")
    private Integer id;

    @Column(name = "id_solicitacao", nullable = false)
    private Integer solicitacaoId;

    @Column(name = "remetente_id", nullable = false)
    private Integer remetenteId;

    @Column(name = "tipo_remetente", nullable = false, length = 20)
    private String tipoRemetente;

    @Column(name = "texto", nullable = false, columnDefinition = "TEXT")
    private String texto;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "imagem_url")
    private String imagemUrl;
}
