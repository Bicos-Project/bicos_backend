package project.bicos.api.dto.mensagem;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MensagemResponseDTO {

    private Integer id;
    private Integer solicitacaoId;
    private Integer remetenteId;
    private String tipoRemetente;
    private String texto;
    private LocalDateTime dataHora;
    private String imagemUrl;
}
