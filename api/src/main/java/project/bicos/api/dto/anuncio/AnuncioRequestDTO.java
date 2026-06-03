package project.bicos.api.dto.anuncio;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnuncioRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
    private String titulo;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Positive(message = "Valor base deve ser maior que zero")
    private BigDecimal valorBase;

    @NotNull(message = "Categoria é obrigatória")
    private Integer categoriaId;

    @NotNull(message = "ID do prestador é obrigatório")
    private Integer prestadorId;
}