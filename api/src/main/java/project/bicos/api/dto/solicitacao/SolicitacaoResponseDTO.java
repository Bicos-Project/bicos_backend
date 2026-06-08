package project.bicos.api.dto.solicitacao;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoResponseDTO {

    private Integer id;
    private String descricao;
    private LocalDate dataSolicitacao;
    private LocalDate dataEstimada;
    private BigDecimal valorSugerido;
    private String status;

    private Integer clienteId;
    private String clienteNome;
    private Double clienteAvaliacao;

    private Integer prestadorId;
    private String prestadorNome;
    private String categoriaNome;
    private Boolean prestadorConfirmouPagamento;
    private Boolean clienteConfirmouPagamento;

    private Boolean clienteAvaliou;
    private Boolean prestadorAvaliou;
}