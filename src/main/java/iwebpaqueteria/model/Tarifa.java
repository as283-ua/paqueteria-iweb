package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @NotNull
    private float coste;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "envio_tarifa",
            joinColumns = { @JoinColumn(name = "fk_tarifa") },
            inverseJoinColumns = {@JoinColumn(name = "fk_envio")})
    private List<Envio> envios;
}
