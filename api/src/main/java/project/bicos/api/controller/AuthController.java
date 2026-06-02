package project.bicos.api.controller;

import project.bicos.api.dto.auth.LoginRequestDTO;
import project.bicos.api.dto.auth.LoginResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Cliente;
import project.bicos.api.models.Prestador;
import project.bicos.api.repository.ClienteRepository;
import project.bicos.api.repository.PrestadorRepository;
import project.bicos.api.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ClienteRepository clienteRepository;
    private final PrestadorRepository prestadorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // -----------------------------------------------------------------
    // POST /auth/login/cliente
    // -----------------------------------------------------------------
    @PostMapping("/login/cliente")
    public ResponseEntity<LoginResponseDTO> loginCliente(
            @RequestBody @Valid LoginRequestDTO dto) {

        // 1. Busca o cliente pelo e-mail
        Cliente cliente = clienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RegraNegocioException(
                        "E-mail ou senha inválidos."));

        // 2. Compara a senha digitada com o hash salvo no banco
        //    passwordEncoder.matches() faz isso de forma segura
        //    NUNCA compare senhas com .equals() — hashes são irreversíveis
        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenhaHash())) {
            throw new RegraNegocioException("E-mail ou senha inválidos.");
        }

        // 3. Gera o token JWT com ID, email e perfil
        String token = jwtService.gerarToken(
                cliente.getId(),
                cliente.getEmail(),
                "CLIENTE"
        );

        // 4. Monta e retorna a resposta
        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "CLIENTE"
        );

        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------
    // POST /auth/login/prestador
    // -----------------------------------------------------------------
    @PostMapping("/login/prestador")
    public ResponseEntity<LoginResponseDTO> loginPrestador(
            @RequestBody @Valid LoginRequestDTO dto) {

        Prestador prestador = prestadorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RegraNegocioException(
                        "E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), prestador.getSenhaHash())) {
            throw new RegraNegocioException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(
                prestador.getId(),
                prestador.getEmail(),
                "PRESTADOR"
        );

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                prestador.getId(),
                prestador.getNome(),
                prestador.getEmail(),
                "PRESTADOR"
        );

        return ResponseEntity.ok(response);
    }
}