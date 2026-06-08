package project.bicos.api.models;

import project.bicos.api.models.enums.StatusSolicitacao;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "solicitacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_seq")
    @SequenceGenerator(
            name = "solicitacao_seq",
            sequenceName = "solicitacao_id_solicitacao_seq",
            allocationSize = 1
    )
    @Column(name = "id_solicitacao")
    private Integer id;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "data_solicitacao")
    private LocalDate dataSolicitacao;

    @Column(name = "data_estimada")
    private LocalDate dataEstimada;

    @Column(name = "valor_sugerido", precision = 10, scale = 2)
    private BigDecimal valorSugerido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusSolicitacao status;

    @Column(name = "prestador_confirmou_pagamento", nullable = false)
    private Boolean prestadorConfirmouPagamento = false;

    @Column(name = "cliente_confirmou_pagamento", nullable = false)
    private Boolean clienteConfirmouPagamento = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestador", nullable = false)
    private Prestador prestador;
}