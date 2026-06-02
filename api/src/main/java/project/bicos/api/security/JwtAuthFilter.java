package project.bicos.api.security;

import project.bicos.api.models.Cliente;
import project.bicos.api.models.Prestador;
import project.bicos.api.repository.ClienteRepository;
import project.bicos.api.repository.PrestadorRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

// OncePerRequestFilter garante que esse filtro roda UMA vez por requisição
// É o "porteiro" que verifica o token antes de qualquer Controller ser executado
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Pega o cabeçalho Authorization da requisição
        String authHeader = request.getHeader("Authorization");

        // 2. Se não há token, deixa passar — o SecurityConfig decidirá
        //    se a rota precisa de autenticação ou não
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Remove o prefixo "Bearer " para pegar só o token
        String token = authHeader.substring(7);

        // 4. Extrai o email e o perfil do token
        String email = jwtService.extrairEmail(token);
        String perfil = jwtService.extrairPerfil(token);

        // 5. Se conseguiu extrair o email e ainda não há autenticação na sessão
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Busca o usuário no banco (cliente ou prestador) para confirmar que existe
            boolean usuarioValido = false;

            if ("CLIENTE".equals(perfil)) {
                Optional<Cliente> cliente = clienteRepository.findByEmail(email);
                usuarioValido = cliente.isPresent()
                        && jwtService.tokenValido(token, email);

            } else if ("PRESTADOR".equals(perfil)) {
                Optional<Prestador> prestador = prestadorRepository.findByEmail(email);
                usuarioValido = prestador.isPresent()
                        && jwtService.tokenValido(token, email);
            }

            // 7. Se tudo ok, registra a autenticação no contexto de segurança
            if (usuarioValido) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + perfil))
                                // ex: ROLE_CLIENTE ou ROLE_PRESTADOR
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Continua a cadeia de filtros (próximo filtro ou o Controller)
        filterChain.doFilter(request, response);
    }
}
