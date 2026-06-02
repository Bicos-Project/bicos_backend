package project.bicos.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Lê os valores do application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // ---------------------------------------------------------------
    // Converte a string do application.properties em uma chave segura
    // O algoritmo HMAC-SHA256 exige uma chave de pelo menos 256 bits
    // ---------------------------------------------------------------
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ---------------------------------------------------------------
    // GERA o token JWT
    // Chamado após o login ser validado com sucesso
    // ---------------------------------------------------------------
    public String gerarToken(Integer userId, String email, String perfil) {
        return Jwts.builder()
                .subject(email)                          // "dono" do token
                .claim("userId", userId)                 // dado extra: ID do usuário
                .claim("perfil", perfil)                 // dado extra: CLIENTE ou PRESTADOR
                .issuedAt(new Date())                    // quando foi gerado
                .expiration(new Date(System.currentTimeMillis() + expiration)) // quando expira
                .signWith(getSigningKey())               // assina com a chave secreta
                .compact();                              // monta a string final
    }

    // ---------------------------------------------------------------
    // VALIDA e extrai o email do token
    // Retorna null se o token for inválido ou expirado
    // ---------------------------------------------------------------
    public String extrairEmail(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())     // verifica a assinatura
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();                  // retorna o email (subject)
        } catch (JwtException e) {
            // Token inválido, expirado ou adulterado
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Extrai o perfil (CLIENTE ou PRESTADOR) do token
    // ---------------------------------------------------------------
    public String extrairPerfil(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("perfil", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    // ---------------------------------------------------------------
    // Verifica se o token ainda é válido (não expirou e email confere)
    // ---------------------------------------------------------------
    public boolean tokenValido(String token, String email) {
        String emailExtraido = extrairEmail(token);
        return emailExtraido != null && emailExtraido.equals(email);
    }
}
