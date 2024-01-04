package iwebpaqueteria.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "historicos")
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

    private String observaciones;

    @ManyToOne
    private Envio envio;

    @ManyToOne
    private Estado estado;

    public Historico() {}

    public Historico(Envio envio, Estado estado) {
        this.envio = envio;
        envio.addHistorico(this);
        this.estado = estado;
        estado.getHistoricos().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
    	this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historico historico = (Historico) o;
        return Objects.equals(fecha, historico.fecha) && id.equals(historico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
