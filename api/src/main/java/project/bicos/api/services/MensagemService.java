package project.bicos.api.services;

import project.bicos.api.dto.mensagem.MensagemRequestDTO;
import project.bicos.api.dto.mensagem.MensagemResponseDTO;
import project.bicos.api.models.Mensagem;
import project.bicos.api.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    @Transactional
    public MensagemResponseDTO enviar(MensagemRequestDTO dto) {
        Mensagem mensagem = new Mensagem();
        mensagem.setSolicitacaoId(dto.getSolicitacaoId());
        mensagem.setRemetenteId(dto.getRemetenteId());
        mensagem.setTipoRemetente(dto.getTipoRemetente());
        mensagem.setTexto(dto.getTexto());
        mensagem.setDataHora(LocalDateTime.now());
        return toResponseDTO(mensagemRepository.save(mensagem));
    }

    @Transactional(readOnly = true)
    public List<MensagemResponseDTO> listarPorSolicitacao(Integer solicitacaoId) {
        return mensagemRepository.findBySolicitacaoIdOrderByDataHoraAsc(solicitacaoId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private MensagemResponseDTO toResponseDTO(Mensagem m) {
        return new MensagemResponseDTO(
                m.getId(),
                m.getSolicitacaoId(),
                m.getRemetenteId(),
                m.getTipoRemetente(),
                m.getTexto(),
                m.getDataHora()
        );
    }
}
