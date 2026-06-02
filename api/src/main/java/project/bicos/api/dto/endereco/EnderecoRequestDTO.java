package project.bicos.api.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRequestDTO {

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "CEP deve ter entre 8 e 9 caracteres")
    private String cep;

    @Size(max = 150, message = "Logradouro deve ter no máximo 150 caracteres")
    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    private String numero;

    @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
    private String complemento;

    // bairro, cidade e estado NÃO entram no Request do cadastro.
    // Serão preenchidos futuramente via consulta automática de CEP (ex: ViaCEP API).
}
