package project.bicos.api.dto.avaliacao;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequestDTO {

    @NotNull(message = "Nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer nota;

    @Size(max = 500, message = "Comentário deve ter no máximo 500 caracteres")
    private String comentario;

    @NotNull(message = "ID da solicitação é obrigatório")
    private Integer solicitacaoId;
}
