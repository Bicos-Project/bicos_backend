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

    // Dados do cliente
    private Integer clienteId;
    private String clienteNome;

    // Dados do anúncio e do prestador
    // O Flutter precisa dessas infos para montar o card da orcamento_page
    private Integer anuncioId;
    private String anuncioTitulo;
    private Integer prestadorId;
    private String prestadorNome;
}