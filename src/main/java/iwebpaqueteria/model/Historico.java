package iwebpaqueteria.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "historicos")
@IdClass(HistoricoId.class)
public class Historico {

    @Id
    private Long envioId;

    @Id
    private Long estadoId;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

    private String observaciones;

    @ManyToOne
    @MapsId("envioId")
    @JoinColumn(name = "envioId")
    private Envio envio;

    @ManyToOne
    @MapsId("estadoId")
    @JoinColumn(name = "estadoId")
    private Estado estado;

    public Historico() {}

    public Historico(Envio envio, Estado estado) {
        this.envio = envio;
        this.estado = estado;

        this.envioId = envio.getId();
        this.estadoId = estado.getId();
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

    public Long getEnvioId() {
        return envioId;
    }

    public void setEnvioId(Long envioId) {
        this.envioId = envioId;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
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
        return Objects.equals(fecha, historico.fecha) && Objects.equals(envioId, historico.envioId) && Objects.equals(estadoId, historico.estadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(envioId, estadoId);
    }
}
