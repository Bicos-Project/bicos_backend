package project.bicos.api.controller;

import project.bicos.api.dto.cliente.ClienteCadastroRequestDTO;
import project.bicos.api.dto.cliente.ClienteResponseDTO;
import project.bicos.api.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// Todos os métodos retornam JSON automaticamente
@RestController
@RequestMapping("/clientes")  // prefixo de todas as rotas deste controller
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // -----------------------------------------------------------------
    // POST /clientes
    // Cadastra um novo cliente
    // @Valid ativa as validações do DTO antes de entrar no método.
    // Se falhar, o GlobalExceptionHandler intercepta automaticamente.
    // -----------------------------------------------------------------
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @RequestBody @Valid ClienteCadastroRequestDTO dto) {

        ClienteResponseDTO response = clienteService.cadastrar(dto);

        // 201 Created = recurso foi criado com sucesso
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -----------------------------------------------------------------
    // GET /clientes/{id}
    // Busca um cliente pelo ID
    // -----------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);

        // 200 OK = requisição bem-sucedida
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------
    // GET /clientes
    // Lista todos os clientes
    // -----------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> response = clienteService.listarTodos();
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------
    // PUT /clientes/{id}
    // Atualiza todos os dados de um cliente existente
    // -----------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteCadastroRequestDTO dto) {

        ClienteResponseDTO response = clienteService.atualizar(id, dto);

        // 200 OK = atualização bem-sucedida
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------------
    // DELETE /clientes/{id}
    // Remove um cliente e seu endereço (orphanRemoval cuida disso)
    // -----------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        clienteService.deletar(id);

        // 204 No Content = operação bem-sucedida, sem corpo na resposta
        return ResponseEntity.noContent().build();
    }
}
