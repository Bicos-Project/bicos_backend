package project.bicos.api.dto.avaliacao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponseDTO {

    private Integer id;
    private Integer nota;
    private String comentario;

    private Integer solicitacaoId;

    private Integer prestadorId;
    private String prestadorNome;

    private Integer clienteId;
    private String clienteNome;
}