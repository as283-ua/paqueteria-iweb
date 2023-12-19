package iwebpaqueteria.model;

import org.hibernate.cfg.NotYetImplementedException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    private Set<Tarifa> tarifas = new HashSet<>();

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
    private Set<Historico> historicos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Set<Tarifa> getTarifas() {
        return tarifas;
    }

    public void setTarifas(Set<Tarifa> tarifas) {
        throw new NotYetImplementedException();
    }

    public Direccion getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(Direccion direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public Direccion getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(Direccion direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public Usuario getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Usuario repartidor) {
        this.repartidor = repartidor;
    }

    public Set<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(Set<Historico> historicos) {
        throw new NotYetImplementedException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Envio envio = (Envio) o;
        return Float.compare(peso, envio.peso) == 0 && Float.compare(precio, envio.precio) == 0 && Objects.equals(id, envio.id) && Objects.equals(observaciones, envio.observaciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, peso, precio);
    }
}
