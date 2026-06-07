package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prestador_foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prestador_foto_seq")
    @SequenceGenerator(
            name = "prestador_foto_seq",
            sequenceName = "prestador_foto_id_foto_seq",
            allocationSize = 1
    )
    @Column(name = "id_foto")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestador", nullable = false)
    private Prestador prestador;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "ordem")
    private Integer ordem;
}
