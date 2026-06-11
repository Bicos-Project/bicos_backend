package project.bicos.api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import project.bicos.api.dto.cliente.ClienteCadastroRequestDTO;
import project.bicos.api.dto.cliente.ClienteResponseDTO;
import project.bicos.api.dto.endereco.EnderecoResponseDTO;
import project.bicos.api.exceptions.RegraNegocioException;
import project.bicos.api.models.Cliente;
import project.bicos.api.models.Endereco;
import project.bicos.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Transactional
    public ClienteResponseDTO cadastrar(ClienteCadastroRequestDTO dto) {

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado para outro cliente.");
        }

        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RegraNegocioException("CPF já cadastrado para outro cliente.");
        }

        Endereco endereco = null;
        if (dto.getEndereco() != null) {
            endereco = new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            endereco.setLatitude(dto.getEndereco().getLatitude());
            endereco.setLongitude(dto.getEndereco().getLongitude());
            endereco.setBairro(dto.getEndereco().getBairro());
            endereco.setCidade(dto.getEndereco().getCidade());
            endereco.setEstado(dto.getEndereco().getEstado());
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(endereco);

        cliente.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        Cliente salvo = clienteRepository.save(cliente);

        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));
        return toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO atualizar(Integer id, ClienteCadastroRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));

        var emailExistente = clienteRepository.findByEmail(dto.getEmail());
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
            throw new RegraNegocioException("E-mail já em uso por outro cliente.");
        }

        var cpfExistente = clienteRepository.findByCpf(dto.getCpf());
        if (cpfExistente.isPresent() && !cpfExistente.get().getId().equals(id)) {
            throw new RegraNegocioException("CPF já em uso por outro cliente.");
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        if (dto.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco() != null ? cliente.getEndereco() : new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            endereco.setLatitude(dto.getEndereco().getLatitude());
            endereco.setLongitude(dto.getEndereco().getLongitude());
            cliente.setEndereco(endereco);
        }

        return toResponseDTO(cliente);
    }

    @Transactional
    public void deletar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));

        if (cliente.getFotoUrl() != null) {
            storageService.deletar(cliente.getFotoUrl());
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public ClienteResponseDTO atualizarFoto(Integer id, MultipartFile file) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));

        if (cliente.getFotoUrl() != null) {
            storageService.deletar(cliente.getFotoUrl());
        }

        String url = storageService.salvar(file);
        cliente.setFotoUrl(url);

        return toResponseDTO(clienteRepository.save(cliente));
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        EnderecoResponseDTO enderecoDTO = null;

        if (cliente.getEndereco() != null) {
            Endereco e = cliente.getEndereco();
            enderecoDTO = new EnderecoResponseDTO(
                    e.getId(),
                    e.getCep(),
                    e.getLogradouro(),
                    e.getNumero(),
                    e.getComplemento(),
                    e.getBairro(),
                    e.getCidade(),
                    e.getEstado(),
                    e.getLatitude(),
                    e.getLongitude()
            );
        }

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getFotoUrl(),
                enderecoDTO
        );
    }
}