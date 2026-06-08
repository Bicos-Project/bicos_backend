package project.bicos.api.controller;

import project.bicos.api.dto.prestador.PrestadorAtualizacaoRequestDTO;
import project.bicos.api.dto.prestador.PrestadorCadastroRequestDTO;
import project.bicos.api.dto.prestador.PrestadorProximoResponseDTO;
import project.bicos.api.dto.prestador.PrestadorResponseDTO;
import project.bicos.api.services.PrestadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<PrestadorResponseDTO>> listarTodos(
            @RequestParam(value = "categoriaNome", required = false) String categoriaNome) {

        List<PrestadorResponseDTO> response = categoriaNome != null
                ? prestadorService.listarPorCategoriaNome(categoriaNome)
                : prestadorService.listarTodos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestadorResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid PrestadorAtualizacaoRequestDTO dto) {

        PrestadorResponseDTO response = prestadorService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        prestadorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PrestadorResponseDTO> adicionarFoto(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        PrestadorResponseDTO response = prestadorService.adicionarFoto(id, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/fotos/{fotoId}")
    public ResponseEntity<PrestadorResponseDTO> removerFoto(
            @PathVariable Integer id,
            @PathVariable Integer fotoId) {

        PrestadorResponseDTO response = prestadorService.removerFoto(id, fotoId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrestadorResponseDTO>> buscar(
            @RequestParam String q) {
        List<PrestadorResponseDTO> response = prestadorService.buscar(q);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<PrestadorProximoResponseDTO>> listarProximos(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "50") double raioKm) {

        List<PrestadorProximoResponseDTO> response =
                prestadorService.listarProximos(lat, lng, raioKm);
        return ResponseEntity.ok(response);
    }
}