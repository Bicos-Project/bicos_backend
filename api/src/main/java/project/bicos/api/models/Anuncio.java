package project.bicos.api.models;

import project.bicos.api.models.enums.StatusAnuncio;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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

    // BigDecimal para valores monetários — nunca use double ou float para dinheiro
    // double tem imprecisão binária: 0.1 + 0.2 = 0.30000000000000004
    @Column(name = "valor_base", precision = 10, scale = 2)
    private BigDecimal valorBase;

    // STRING salva "ativo"/"inativo" no banco — legível e seguro
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private StatusAnuncio status = StatusAnuncio.ativo; // padrão ao criar

    // Relacionamento com Prestador — um prestador tem muitos anúncios
    // @ManyToOne não usa cascade: não faz sentido deletar o prestador ao deletar anúncio
    // FetchType.LAZY: só carrega o Prestador do banco quando você chamar .getPrestador()
    // Sem LAZY, toda busca de anúncio traria o objeto Prestador junto — desperdício
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestador", nullable = false)
    private Prestador prestador;

    // Relacionamento com Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}
