package project.bicos.api.exceptions;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// Esse é o objeto que o cliente recebe quando algo dá errado.
// Formato fixo, previsível, fácil de tratar no front-end.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private int status;           // código HTTP (400, 404, 409, 500...)
    private String erro;          // título curto do erro
    private List<String> mensagens; // lista de mensagens (pode ter múltiplos erros de validação)
    private LocalDateTime timestamp; // quando o erro ocorreu
}