package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "endereco_seq")
    @SequenceGenerator(
            name = "endereco_seq",
            sequenceName = "endereco_id_endereco_seq",
            allocationSize = 1
    )
    @Column(name = "id_endereco")
    private Integer id;

    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @Column(name = "logradouro", length = 150)
    private String logradouro;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}