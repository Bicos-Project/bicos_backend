package project.bicos.api.dto.solicitacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoRequestDTO {

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    // Data escolhida pelo cliente na tela de solicitação
    private LocalDate dataSolicitacao;

    @NotNull(message = "ID do cliente é obrigatório")
    private Integer clienteId;

    @NotNull(message = "ID do anúncio é obrigatório")
    private Integer anuncioId;
}
