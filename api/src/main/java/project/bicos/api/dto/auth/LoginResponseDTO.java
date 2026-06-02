package project.bicos.api.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;       // JWT que o Flutter vai guardar e usar
    private String tipo;        // "Bearer" — padrão do mercado
    private Integer id;         // ID do usuário logado
    private String nome;        // Para o Flutter mostrar "Olá, João!"
    private String email;
    private String perfil;      // "CLIENTE" ou "PRESTADOR"
}
