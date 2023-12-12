package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String codigoPostal;

    @NotNull
    private String localidad;

    @NotNull
    private String provincia;

    @NotNull
    private int numero;

    @NotNull
    private int planta;

    @NotNull
    private String calle;

    @NotNull
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "direccionOrigen")
    private Set<Envio> enviosOrigen;

    @OneToMany(mappedBy = "direccionDestino")
    private Set<Envio> enviosDestino;
}
