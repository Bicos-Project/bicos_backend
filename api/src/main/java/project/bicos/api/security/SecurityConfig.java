package project.bicos.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desativa CSRF — não precisamos para APIs REST stateless
                // CSRF protege formulários HTML tradicionais, não APIs com JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Stateless: o servidor não guarda sessão.
                // Cada requisição se autentica pelo token — sem cookies de sessão
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Regras de autorização por rota
                .authorizeHttpRequests(auth -> auth

                        // ── ROTAS PÚBLICAS (não precisam de token) ──────────────
                        .requestMatchers(HttpMethod.POST, "/clientes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/prestadores").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        // ── ROTAS PROTEGIDAS ────────────────────────────────────
                        // Qualquer outra requisição precisa estar autenticado
                        .anyRequest().authenticated()
                )

                // Registra nosso filtro JWT ANTES do filtro padrão de autenticação
                // do Spring Security
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean do BCrypt — agora podemos injetar PasswordEncoder nos Services
    // É aqui que substituímos o "HASH_SIMULADO:" que usamos antes
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
