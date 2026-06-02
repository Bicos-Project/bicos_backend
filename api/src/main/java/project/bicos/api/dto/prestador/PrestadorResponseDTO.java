package project.bicos.api.dto.prestador;

import project.bicos.api.dto.endereco.EnderecoResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorResponseDTO {

    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String descricao;
    private EnderecoResponseDTO endereco;
}
