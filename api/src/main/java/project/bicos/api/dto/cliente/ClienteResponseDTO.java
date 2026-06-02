package project.bicos.api.dto.cliente;

import project.bicos.api.dto.endereco.EnderecoResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private EnderecoResponseDTO endereco;
}
