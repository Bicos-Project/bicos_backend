package project.bicos.api.dto.prestador;

import project.bicos.api.dto.categoria.CategoriaResponseDTO;
import project.bicos.api.dto.endereco.EnderecoResponseDTO;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
    private String especialidade;
    private BigDecimal avaliacao;
    private CategoriaResponseDTO categoria;
    private List<PrestadorFotoResponseDTO> fotos;
    private EnderecoResponseDTO endereco;
}
