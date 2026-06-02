package project.bicos.api.exceptions;

// Extends RuntimeException para não precisar de try/catch obrigatório.
// Usamos ela para erros de negócio previstos (ex: e-mail duplicado).
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
