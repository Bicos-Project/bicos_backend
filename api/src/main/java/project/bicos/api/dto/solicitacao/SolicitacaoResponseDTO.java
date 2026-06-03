package project.bicos.api.dto.solicitacao;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoResponseDTO {

    private Integer id;
    private String descricao;
    private LocalDate dataSolicitacao;
    private String status;

    private Integer clienteId;
    private String clienteNome;

    private Integer anuncioId;
    private String anuncioTitulo;
    private Integer prestadorId;
    private String prestadorNome;
}