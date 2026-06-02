package project.bicos.api.dto.avaliacao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MediaAvaliacaoResponseDTO {

    private Integer prestadorId;
    private String prestadorNome;
    private Double mediaNotas;
    private Integer totalAvaliacoes;
}
