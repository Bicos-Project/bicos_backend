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

    // @Positive garante que o valor seja maior que zero
    @Positive(message = "Valor base deve ser maior que zero")
    private BigDecimal valorBase;

    // ID da categoria escolhida pelo prestador nos chips da tela
    @NotNull(message = "Categoria é obrigatória")
    private Integer categoriaId;

    // ID do prestador que está criando o anúncio
    // Quando tivermos JWT, esse campo virá do token automaticamente
    // Por enquanto o front-end envia explicitamente
    @NotNull(message = "ID do prestador é obrigatório")
    private Integer prestadorId;
}