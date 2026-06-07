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

    @PostMapping("/login/cliente")
    public ResponseEntity<LoginResponseDTO> loginCliente(
            @RequestBody @Valid LoginRequestDTO dto) {

        Cliente cliente = clienteRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RegraNegocioException(
                        "E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), cliente.getSenhaHash())) {
            throw new RegraNegocioException("E-mail ou senha inválidos.");
        }

        String token = jwtService.gerarToken(
                cliente.getId(),
                cliente.getEmail(),
                "CLIENTE"
        );

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "CLIENTE",
                cliente.getFotoUrl()
        );

        return ResponseEntity.ok(response);
    }

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

        String fotoUrl = prestador.getFotos() != null && !prestador.getFotos().isEmpty()
                ? prestador.getFotos().get(0).getUrl()
                : null;

        LoginResponseDTO response = new LoginResponseDTO(
                token,
                "Bearer",
                prestador.getId(),
                prestador.getNome(),
                prestador.getEmail(),
                "PRESTADOR",
                fotoUrl
        );

        return ResponseEntity.ok(response);
    }
}