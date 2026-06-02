package project.bicos.api.services;

import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequiredArgsConstructor  // Lombok gera construtor com todos os campos final — padrão para injeção de dependência
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    // -------------------------------------------------------------------------
    // CADASTRO
    // -------------------------------------------------------------------------

    @Transactional  // Garante que cliente + endereço sejam salvos juntos ou nenhum é salvo
    public ClienteResponseDTO cadastrar(ClienteCadastroRequestDTO dto) {

        // --- Regra 1: e-mail único entre clientes ---
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RegraNegocioException("E-mail já cadastrado para outro cliente.");
        }

        // --- Regra 2: CPF único entre clientes ---
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RegraNegocioException("CPF já cadastrado para outro cliente.");
        }

        // --- Monta a entidade Endereco a partir do DTO ---
        Endereco endereco = null;
        if (dto.getEndereco() != null) {
            endereco = new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            // bairro, cidade e estado ficam nulos até integração com API de CEP
        }

        // --- Monta a entidade Cliente ---
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setEndereco(endereco);

        // --- Hash da senha ---
        // Em produção, substitua a linha abaixo por:
        // cliente.setSenhaHash(passwordEncoder.encode(dto.getSenha()));
        // onde passwordEncoder é um BCryptPasswordEncoder injetado via @Bean no SecurityConfig.
        // Por ora, simulamos com um prefixo para deixar claro que não é texto puro:
        cliente.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        // --- Persiste (cascade salva o endereço automaticamente) ---
        Cliente salvo = clienteRepository.save(cliente);

        return toResponseDTO(salvo);
    }

    // -------------------------------------------------------------------------
    // BUSCA POR ID
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)  // readOnly = true: otimiza leitura, sem lock desnecessário
    public ClienteResponseDTO buscarPorId(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));
        return toResponseDTO(cliente);
    }

    // -------------------------------------------------------------------------
    // LISTAR TODOS
    // -------------------------------------------------------------------------

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)  // para cada Cliente, aplica toResponseDTO
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // ATUALIZAR
    // -------------------------------------------------------------------------

    @Transactional
    public ClienteResponseDTO atualizar(Integer id, ClienteCadastroRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Cliente não encontrado com ID: " + id));

        // Verifica se o novo e-mail já pertence a OUTRO cliente
        clienteRepository.findByEmail(dto.getEmail())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new RegraNegocioException("E-mail já em uso por outro cliente."); });

        // Verifica se o novo CPF já pertence a OUTRO cliente
        clienteRepository.findByCpf(dto.getCpf())
                .filter(c -> !c.getId().equals(id))
                .ifPresent(c -> { throw new RegraNegocioException("CPF já em uso por outro cliente."); });

        // Atualiza os dados
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setCpf(dto.getCpf());
        cliente.setTelefone(dto.getTelefone());
        cliente.setSenhaHash(passwordEncoder.encode(dto.getSenha()));

        // Atualiza endereço se fornecido
        if (dto.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco() != null ? cliente.getEndereco() : new Endereco();
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            cliente.setEndereco(endereco);
        }

        // Não precisamos chamar save() explicitamente aqui.
        // Como o objeto foi carregado dentro de um @Transactional,
        // o JPA detecta as mudanças automaticamente (dirty checking)
        // e emite o UPDATE ao final do método.
        return toResponseDTO(cliente);
    }

    // -------------------------------------------------------------------------
    // DELETAR
    // -------------------------------------------------------------------------

    @Transactional
    public void deletar(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new RegraNegocioException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
        // orphanRemoval = true na entidade garante que o Endereco
        // associado também será removido automaticamente
    }

    // -------------------------------------------------------------------------
    // MÉTODO AUXILIAR PRIVADO: Entity → ResponseDTO
    // -------------------------------------------------------------------------

    // Privado pois só faz sentido dentro deste Service.
    // Centraliza a conversão — se o DTO mudar, só muda aqui.
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
                    e.getEstado()
            );
        }

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getCpf(),
                cliente.getTelefone(),
                enderecoDTO
        );
    }
}