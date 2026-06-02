package project.bicos.api.controller;

import project.bicos.api.dto.prestador.PrestadorCadastroRequestDTO;
import project.bicos.api.dto.prestador.PrestadorResponseDTO;
import project.bicos.api.services.PrestadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestadores")
@RequiredArgsConstructor
public class PrestadorController {

    private final PrestadorService prestadorService;

    @PostMapping
    public ResponseEntity<PrestadorResponseDTO> cadastrar(
            @RequestBody @Valid PrestadorCadastroRequestDTO dto) {

        PrestadorResponseDTO response = prestadorService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestadorResponseDTO> buscarPorId(@PathVariable Integer id) {
        PrestadorResponseDTO response = prestadorService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PrestadorResponseDTO>> listarTodos() {
        List<PrestadorResponseDTO> response = prestadorService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestadorResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PrestadorCadastroRequestDTO dto) {

        PrestadorResponseDTO response = prestadorService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        prestadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}