package project.bicos.api.dto.categoria;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {

    private Integer id;
    private String nome;
    private String descricao;
}