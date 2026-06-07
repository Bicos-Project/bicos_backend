package project.bicos.api.controller;

import project.bicos.api.dto.cliente.ClienteCadastroRequestDTO;
import project.bicos.api.dto.cliente.ClienteResponseDTO;
import project.bicos.api.services.ClienteService;
import project.bicos.api.services.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final StorageService storageService;

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

    @PutMapping(value = "/{id}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteResponseDTO> atualizarFoto(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ClienteResponseDTO response = clienteService.atualizarFoto(id, file);
        return ResponseEntity.ok(response);
    }
}
