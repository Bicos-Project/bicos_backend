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

    // Dados da solicitação avaliada
    private Integer solicitacaoId;

    // Dados do prestador avaliado — para exibir no perfil
    private Integer prestadorId;
    private String prestadorNome;

    // Dados de quem avaliou
    private Integer clienteId;
    private String clienteNome;
}