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

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String email = jwtService.extrairEmail(token);
        String perfil = jwtService.extrairPerfil(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

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

            if (usuarioValido) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + perfil))
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
