package project.bicos.api.controller;

import project.bicos.api.dto.solicitacao.SolicitacaoRequestDTO;
import project.bicos.api.dto.solicitacao.SolicitacaoResponseDTO;
import project.bicos.api.services.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(
            @RequestBody @Valid SolicitacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitacaoService.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
    }

    @GetMapping(params = "clienteId")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorCliente(
            @RequestParam Integer clienteId) {
        return ResponseEntity.ok(solicitacaoService.listarPorCliente(clienteId));
    }

    @GetMapping(params = "anuncioId")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorAnuncio(
            @RequestParam Integer anuncioId) {
        return ResponseEntity.ok(solicitacaoService.listarPorAnuncio(anuncioId));
    }

    @GetMapping(params = "prestadorId")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorPrestador(
            @RequestParam Integer prestadorId) {
        return ResponseEntity.ok(solicitacaoService.listarPorPrestador(prestadorId));
    }

    @PatchMapping("/{id}/avancar")
    public ResponseEntity<SolicitacaoResponseDTO> avancarStatus(
            @PathVariable Integer id) {
        return ResponseEntity.ok(solicitacaoService.avancarStatus(id));
    }
}
