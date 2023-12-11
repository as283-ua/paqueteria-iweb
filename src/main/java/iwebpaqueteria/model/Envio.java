package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "envios")
public class Envio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private float peso;

    @NotNull
    private float precio;

    private String observaciones;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "envio_tarifa",
            joinColumns = { @JoinColumn(name = "fk_envio") },
            inverseJoinColumns = {@JoinColumn(name = "fk_tarifa")})
    private Set<Tarifa> tarifas;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "direccion_origen_id")
    private Direccion direccionOrigen;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "direccion_destino_id")
    private Direccion direccionDestino;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Usuario repartidor;

    @OneToMany(mappedBy = "envio")
    private Set<Historico> historicos;
}
