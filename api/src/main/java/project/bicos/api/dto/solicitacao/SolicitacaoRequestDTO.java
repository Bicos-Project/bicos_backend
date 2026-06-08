package project.bicos.api.dto.solicitacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoRequestDTO {

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    private LocalDate dataSolicitacao;

    private LocalDate dataEstimada;

    private BigDecimal valorSugerido;

    @NotNull(message = "ID do cliente é obrigatório")
    private Integer clienteId;

    @NotNull(message = "ID do prestador é obrigatório")
    private Integer prestadorId;
}
