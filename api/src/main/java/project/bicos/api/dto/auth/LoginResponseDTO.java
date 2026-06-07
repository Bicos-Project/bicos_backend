package project.bicos.api.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String tipo;        // "Bearer" — padrão do mercado
    private Integer id;
    private String nome;        // Para o Flutter mostrar "Olá, João!"
    private String email;
    private String perfil;      // "CLIENTE" ou "PRESTADOR"
    private String fotoUrl;     // URL da foto de perfil
}
