package project.bicos.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "cliente")
@Entity(name = "Cliente")
public class Cliente {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String senha_hash;
    private String telefone;
    // Verificar questão do endereço
}
