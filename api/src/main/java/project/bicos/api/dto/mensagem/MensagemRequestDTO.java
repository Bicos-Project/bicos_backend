package project.bicos.api.dto.mensagem;

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

    @NotNull(message = "Tipo do remetente é obrigatório")
    private String tipoRemetente;

    private String texto;
}
