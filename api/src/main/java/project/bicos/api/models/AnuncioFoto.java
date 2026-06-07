package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anuncio_foto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnuncioFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anuncio_foto_seq")
    @SequenceGenerator(
            name = "anuncio_foto_seq",
            sequenceName = "anuncio_foto_id_foto_seq",
            allocationSize = 1
    )
    @Column(name = "id_foto")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anuncio", nullable = false)
    private Anuncio anuncio;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "ordem")
    private Integer ordem;
}
