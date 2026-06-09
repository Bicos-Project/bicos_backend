package project.bicos.api.models;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FavoritoId implements Serializable {
    @Column(name = "id_cliente")
    private Integer clienteId;

    @Column(name = "id_prestador")
    private Integer prestadorId;
}
