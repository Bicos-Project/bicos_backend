package project.bicos.api.dto.anuncio;

import project.bicos.api.dto.categoria.CategoriaResponseDTO;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnuncioResponseDTO {

    private Integer id;
    private String titulo;
    private String descricao;
    private BigDecimal valorBase;
    private String status;

    private CategoriaResponseDTO categoria;

    private Integer prestadorId;
    private String prestadorNome;
}