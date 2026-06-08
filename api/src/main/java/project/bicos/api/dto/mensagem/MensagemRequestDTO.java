package project.bicos.api.dto.mensagem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemRequestDTO {

    @NotNull(message = "ID da solicitação é obrigatório")
    private Integer solicitacaoId;

    @NotNull(message = "ID do remetente é obrigatório")
    private Integer remetenteId;

    @NotBlank(message = "Tipo do remetente é obrigatório")
    private String tipoRemetente;

    @NotBlank(message = "Texto da mensagem é obrigatório")
    private String texto;
}
