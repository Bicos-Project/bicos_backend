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

    // POST /solicitacoes — cliente solicita orçamento
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(
            @RequestBody @Valid SolicitacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitacaoService.criar(dto));
    }

    // GET /solicitacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> buscarPorId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
    }

    // GET /solicitacoes?clienteId=3 — histórico do cliente
    @GetMapping(params = "clienteId")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorCliente(
            @RequestParam Integer clienteId) {
        return ResponseEntity.ok(solicitacaoService.listarPorCliente(clienteId));
    }

    // GET /solicitacoes?anuncioId=7 — solicitações recebidas pelo prestador
    @GetMapping(params = "anuncioId")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorAnuncio(
            @RequestParam Integer anuncioId) {
        return ResponseEntity.ok(solicitacaoService.listarPorAnuncio(anuncioId));
    }

    // PATCH /solicitacoes/{id}/avancar — avança o status na máquina de estados
    // PATCH porque é uma mudança parcial (só o status muda)
    @PatchMapping("/{id}/avancar")
    public ResponseEntity<SolicitacaoResponseDTO> avancarStatus(
            @PathVariable Integer id) {
        return ResponseEntity.ok(solicitacaoService.avancarStatus(id));
    }
}
