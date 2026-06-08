package project.bicos.api.controller;

import project.bicos.api.dto.mensagem.MensagemRequestDTO;
import project.bicos.api.dto.mensagem.MensagemResponseDTO;
import project.bicos.api.services.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;

    @PostMapping
    public ResponseEntity<MensagemResponseDTO> enviar(
            @RequestBody @Valid MensagemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mensagemService.enviar(dto));
    }

    @GetMapping(params = "solicitacaoId")
    public ResponseEntity<List<MensagemResponseDTO>> listarPorSolicitacao(
            @RequestParam Integer solicitacaoId) {
        return ResponseEntity.ok(mensagemService.listarPorSolicitacao(solicitacaoId));
    }
}
