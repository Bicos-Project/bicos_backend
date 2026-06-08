package project.bicos.api.controller;

import project.bicos.api.dto.avaliacao.AvaliacaoRequestDTO;
import project.bicos.api.dto.avaliacao.AvaliacaoResponseDTO;
import project.bicos.api.dto.avaliacao.MediaAvaliacaoResponseDTO;
import project.bicos.api.services.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> criar(
            @RequestBody @Valid AvaliacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avaliacaoService.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @GetMapping("/solicitacao/{solicitacaoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> buscarPorSolicitacao(
            @PathVariable Integer solicitacaoId) {
        return ResponseEntity.ok(
                avaliacaoService.buscarPorSolicitacao(solicitacaoId));
    }

    @GetMapping(params = "prestadorId")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarPorPrestador(
            @RequestParam Integer prestadorId) {
        return ResponseEntity.ok(
                avaliacaoService.listarPorPrestador(prestadorId));
    }

    @GetMapping("/media")
    public ResponseEntity<MediaAvaliacaoResponseDTO> calcularMedia(
            @RequestParam Integer prestadorId) {
        return ResponseEntity.ok(avaliacaoService.calcularMedia(prestadorId));
    }
}