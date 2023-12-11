package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique=true)
    private String email;

    @NotNull
    private String contrasenya;

    @NotNull
    private String nombre;

    @NotNull
    @Column(unique=true)
    private String telefono;

    private String APIKey;
    @OneToOne(mappedBy = "usuario")
    private Direccion direccion;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @OneToMany(mappedBy = "repartidor")
    private Set<Envio> envios;
}
