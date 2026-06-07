package project.bicos.api.models;

import project.bicos.api.models.enums.StatusAnuncio;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "anuncio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anuncio_seq")
    @SequenceGenerator(
            name = "anuncio_seq",
            sequenceName = "anuncio_id_anuncio_seq",
            allocationSize = 1
    )
    @Column(name = "id_anuncio")
    private Integer id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "valor_base", precision = 10, scale = 2)
    private BigDecimal valorBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private StatusAnuncio status = StatusAnuncio.ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestador", nullable = false)
    private Prestador prestador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<AnuncioFoto> fotos = new ArrayList<>();
}
