package project.bicos.api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.bicos.api.dto.endereco.EnderecoResponseDTO;
import project.bicos.api.dto.prestador.PrestadorCadastroRequestDTO;
import project.bicos.api.dto.prestador.PrestadorFotoResponseDTO;
import project.bicos.api.dto.prestador.PrestadorResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Endereco;
import project.bicos.api.models.Prestador;
import project.bicos.api.models.PrestadorFoto;
import project.bicos.api.repository.PrestadorFotoRepository;
import project.bicos.api.repository.PrestadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrestadorService {

    private final PrestadorRepository prestadorRepository;
    private final PrestadorFotoRepository prestadorFotoRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Transactional
    public PrestadorResponseDTO cadastrar(PrestadorCadastroRequestDTO dto) {

        if (prestadorRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado para outro prestador.");
        }

        if (prestadorRepository.existsByCpf(dto.getCpf())) {
            throw new RegraNegocioException("CPF já cadastrado para outro prestador.");
        }

        Endereco endereco = null;
        if (dto.getEndereco() != null) {
            endereco = new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
        }

        Prestador prestador = new Prestador();
        prestador.setNome(dto.getNome());
        prestador.setEmail(dto.getEmail());
        prestador.setCpf(dto.getCpf());
        prestador.setTelefone(dto.getTelefone());
        prestador.setDescricao(dto.getDescricao());
        prestador.setEspecialidade(dto.getEspecialidade());
        prestador.setEndereco(endereco);

        prestador.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        Prestador salvo = prestadorRepository.save(prestador);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public PrestadorResponseDTO buscarPorId(Integer id) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Prestador não encontrado com ID: " + id));
        return toResponseDTO(prestador);
    }

    @Transactional(readOnly = true)
    public List<PrestadorResponseDTO> listarTodos() {
        return prestadorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrestadorResponseDTO> listarPorCategoriaNome(String nomeCategoria) {
        return prestadorRepository.findByCategoriaNome(nomeCategoria)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PrestadorResponseDTO atualizar(Integer id, PrestadorCadastroRequestDTO dto) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Prestador não encontrado com ID: " + id));

        prestadorRepository.findByEmail(dto.getEmail())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new RegraNegocioException("E-mail já em uso por outro prestador."); });

        prestadorRepository.findByCpf(dto.getCpf())
                .filter(p -> !p.getId().equals(id))
                .ifPresent(p -> { throw new RegraNegocioException("CPF já em uso por outro prestador."); });

        prestador.setNome(dto.getNome());
        prestador.setEmail(dto.getEmail());
        prestador.setCpf(dto.getCpf());
        prestador.setTelefone(dto.getTelefone());
        prestador.setDescricao(dto.getDescricao());
        prestador.setEspecialidade(dto.getEspecialidade());
        prestador.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        if (dto.getEndereco() != null) {
            Endereco endereco = prestador.getEndereco() != null ? prestador.getEndereco() : new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            prestador.setEndereco(endereco);
        }

        return toResponseDTO(prestador);
    }

    @Transactional
    public void deletar(Integer id) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Prestador não encontrado com ID: " + id));

        if (prestador.getFotos() != null) {
            prestador.getFotos().forEach(foto -> storageService.deletar(foto.getUrl()));
        }
        prestadorRepository.deleteById(id);
    }

    @Transactional
    public PrestadorResponseDTO adicionarFoto(Integer id, MultipartFile file) {
        Prestador prestador = prestadorRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Prestador não encontrado com ID: " + id));

        String url = storageService.salvar(file);
        int ordem = prestador.getFotos().isEmpty()
                ? 0
                : prestador.getFotos().stream().mapToInt(PrestadorFoto::getOrdem).max().orElse(0) + 1;

        PrestadorFoto foto = new PrestadorFoto();
        foto.setUrl(url);
        foto.setOrdem(ordem);
        foto.setPrestador(prestador);
        prestador.getFotos().add(foto);

        return toResponseDTO(prestadorRepository.save(prestador));
    }

    @Transactional
    public PrestadorResponseDTO removerFoto(Integer prestadorId, Integer fotoId) {
        Prestador prestador = prestadorRepository.findById(prestadorId)
                .orElseThrow(() -> new RegraNegocioException("Prestador não encontrado com ID: " + prestadorId));

        PrestadorFoto foto = prestadorFotoRepository.findById(fotoId)
                .orElseThrow(() -> new RegraNegocioException("Foto não encontrada com ID: " + fotoId));

        if (!foto.getPrestador().getId().equals(prestadorId)) {
            throw new RegraNegocioException("Esta foto não pertence ao prestador informado.");
        }

        storageService.deletar(foto.getUrl());
        prestador.getFotos().remove(foto);
        prestadorFotoRepository.delete(foto);

        return toResponseDTO(prestador);
    }

    private PrestadorResponseDTO toResponseDTO(Prestador prestador) {
        EnderecoResponseDTO enderecoDTO = null;

        if (prestador.getEndereco() != null) {
            Endereco e = prestador.getEndereco();
            enderecoDTO = new EnderecoResponseDTO(
                    e.getId(),
                    e.getCep(),
                    e.getLogradouro(),
                    e.getNumero(),
                    e.getComplemento(),
                    e.getBairro(),
                    e.getCidade(),
                    e.getEstado()
            );
        }

        List<PrestadorFotoResponseDTO> fotosDTO = prestador.getFotos() != null
                ? prestador.getFotos().stream()
                    .map(f -> new PrestadorFotoResponseDTO(f.getId(), f.getUrl()))
                    .collect(Collectors.toList())
                : new ArrayList<>();

        BigDecimal avaliacao = prestador.getAvaliacao() != null
                ? prestador.getAvaliacao()
                : BigDecimal.ZERO;

        return new PrestadorResponseDTO(
                prestador.getId(),
                prestador.getNome(),
                prestador.getEmail(),
                prestador.getCpf(),
                prestador.getTelefone(),
                prestador.getDescricao(),
                prestador.getEspecialidade(),
                avaliacao,
                fotosDTO,
                enderecoDTO
        );
    }
}