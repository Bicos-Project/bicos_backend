package project.bicos.api.controller;

import project.bicos.api.dto.anuncio.AnuncioRequestDTO;
import project.bicos.api.dto.anuncio.AnuncioResponseDTO;
import project.bicos.api.services.AnuncioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/anuncios")
@RequiredArgsConstructor
public class AnuncioController {

    private final AnuncioService anuncioService;

    @PostMapping
    public ResponseEntity<AnuncioResponseDTO> criar(
            @RequestBody @Valid AnuncioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(anuncioService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AnuncioResponseDTO>> listarAtivos() {
        return ResponseEntity.ok(anuncioService.listarAtivos());
    }

    @GetMapping(params = "categoriaId")
    public ResponseEntity<List<AnuncioResponseDTO>> listarPorCategoria(
            @RequestParam Integer categoriaId) {
        return ResponseEntity.ok(anuncioService.listarPorCategoria(categoriaId));
    }

    @GetMapping(params = "prestadorId")
    public ResponseEntity<List<AnuncioResponseDTO>> listarPorPrestador(
            @RequestParam Integer prestadorId) {
        return ResponseEntity.ok(anuncioService.listarPorPrestador(prestadorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(anuncioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnuncioResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AnuncioRequestDTO dto) {
        return ResponseEntity.ok(anuncioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<AnuncioResponseDTO> desativar(@PathVariable Integer id) {
        return ResponseEntity.ok(anuncioService.desativar(id));
    }
}
