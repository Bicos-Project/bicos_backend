package project.bicos.api.controller;

import project.bicos.api.dto.prestador.PrestadorResponseDTO;
import project.bicos.api.services.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<PrestadorResponseDTO>> listar(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(favoritoService.listarPorCliente(clienteId));
    }

    @GetMapping("/{clienteId}/ids")
    public ResponseEntity<List<Integer>> listarIds(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(favoritoService.listarIdsPorCliente(clienteId));
    }

    @GetMapping("/{clienteId}/check")
    public ResponseEntity<Map<String, Boolean>> check(
            @PathVariable Integer clienteId,
            @RequestParam Integer prestadorId) {
        boolean favorito = favoritoService.isFavorito(clienteId, prestadorId);
        return ResponseEntity.ok(Map.of("favorito", favorito));
    }

    @PostMapping("/{clienteId}/{prestadorId}")
    public ResponseEntity<Map<String, Boolean>> toggle(
            @PathVariable Integer clienteId,
            @PathVariable Integer prestadorId) {
        boolean result = favoritoService.toggle(clienteId, prestadorId);
        return ResponseEntity.ok(Map.of("favorito", result));
    }

    @DeleteMapping("/{clienteId}/{prestadorId}")
    public ResponseEntity<Void> remover(
            @PathVariable Integer clienteId,
            @PathVariable Integer prestadorId) {
        favoritoService.remover(clienteId, prestadorId);
        return ResponseEntity.noContent().build();
    }
}
