package project.bicos.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// @RestControllerAdvice = @ControllerAdvice + @ResponseBody
// Intercepta exceções lançadas em QUALQUER controller da aplicação.
// Sem isso, o Spring retornaria HTML de erro ou stack traces para o cliente.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -----------------------------------------------------------------
    // Erros de validação do Bean Validation (@NotBlank, @Email, etc.)
    // Lançado automaticamente quando um @Valid falha no Controller
    // -----------------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Coleta todas as mensagens de erro de todos os campos inválidos
        List<String> mensagens = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage) // pega a mensagem do @NotBlank, @Email etc.
                .collect(Collectors.toList());

        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),  // 400
                "Requisição inválida",
                mensagens,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }

    // -----------------------------------------------------------------
    // Erros de regra de negócio (e-mail duplicado, não encontrado etc.)
    // Lançado explicitamente nos nossos Services
    // -----------------------------------------------------------------
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiErrorResponse> handleRegraNegocio(
            RegraNegocioException ex) {

        // Precisamos distinguir "não encontrado" (404) de "conflito" (409).
        // Fazemos isso inspecionando a mensagem — simples e funcional para o projeto.
        HttpStatus status = ex.getMessage().contains("não encontrado")
                ? HttpStatus.NOT_FOUND        // 404
                : HttpStatus.CONFLICT;        // 409

        ApiErrorResponse response = new ApiErrorResponse(
                status.value(),
                status == HttpStatus.NOT_FOUND ? "Recurso não encontrado" : "Conflito de dados",
                List.of(ex.getMessage()),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }

    // -----------------------------------------------------------------
    // Qualquer outro erro inesperado (null pointer, banco fora etc.)
    // Captura tudo que não foi tratado acima — "catch-all"
    // -----------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleErroGeral(Exception ex) {

        // NUNCA exponha ex.getMessage() aqui em produção —
        // pode vazar detalhes internos (queries SQL, paths, etc.)
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Erro interno do servidor",
                List.of("Ocorreu um erro inesperado. Tente novamente mais tarde."),
                LocalDateTime.now()
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
