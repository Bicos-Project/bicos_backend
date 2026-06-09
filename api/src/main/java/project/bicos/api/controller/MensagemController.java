package project.bicos.api.controller;

import project.bicos.api.dto.mensagem.MensagemRequestDTO;
import project.bicos.api.dto.mensagem.MensagemResponseDTO;
import project.bicos.api.services.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MensagemResponseDTO> enviar(
            @RequestParam("solicitacaoId") Integer solicitacaoId,
            @RequestParam("remetenteId") Integer remetenteId,
            @RequestParam("tipoRemetente") String tipoRemetente,
            @RequestParam(value = "texto", required = false) String texto,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {

        MensagemRequestDTO dto = new MensagemRequestDTO();
        dto.setSolicitacaoId(solicitacaoId);
        dto.setRemetenteId(remetenteId);
        dto.setTipoRemetente(tipoRemetente);
        dto.setTexto(texto);

        MensagemResponseDTO response = imagem != null && !imagem.isEmpty()
                ? mensagemService.enviarComFoto(dto, imagem)
                : mensagemService.enviar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(params = "solicitacaoId")
    public ResponseEntity<List<MensagemResponseDTO>> listarPorSolicitacao(
            @RequestParam Integer solicitacaoId) {
        return ResponseEntity.ok(mensagemService.listarPorSolicitacao(solicitacaoId));
    }
}
