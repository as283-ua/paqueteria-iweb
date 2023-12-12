package iwebpaqueteria.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "historicos")
@IdClass(HistoricoId.class)
public class Historico {
    @EmbeddedId
    private HistoricoId id;

    @Id
    private Long envioId;

    @Id
    private Long estadoId;

    @Column(insertable = false, updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    @MapsId("envioId")
    @JoinColumn(name = "envioId")
    private Envio envio;

    @ManyToOne
    @MapsId("estadoId")
    @JoinColumn(name = "estadoId")
    private Estado estado;

    public HistoricoId getId() {
        return id;
    }

    public void setId(HistoricoId id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historico historico = (Historico) o;
        return Objects.equals(id, historico.id) && Objects.equals(fecha, historico.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
