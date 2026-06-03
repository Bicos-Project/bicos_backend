package project.bicos.api.exceptions;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private int status;
    private String erro;
    private List<String> mensagens;
    private LocalDateTime timestamp;
}