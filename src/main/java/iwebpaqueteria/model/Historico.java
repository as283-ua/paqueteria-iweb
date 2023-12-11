package iwebpaqueteria.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historicos")
@IdClass(HistoricoId.class)
public class Historico {
    @EmbeddedId
    private HistoricoId id;

    @Column(insertable = false, updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    @MapsId("envioId")
    @JoinColumn(name = "envio_id")
    private Envio envio;

    @ManyToOne
    @MapsId("estadoId")
    @JoinColumn(name = "estado_id")
    private Estado estado;
}
