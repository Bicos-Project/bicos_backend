package project.bicos.api.controller;

import project.bicos.api.dto.cliente.ClienteCadastroRequestDTO;
import project.bicos.api.dto.cliente.ClienteResponseDTO;
import project.bicos.api.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @RequestBody @Valid ClienteCadastroRequestDTO dto) {

        ClienteResponseDTO response = clienteService.cadastrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> response = clienteService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteCadastroRequestDTO dto) {

        ClienteResponseDTO response = clienteService.atualizar(id, dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}
