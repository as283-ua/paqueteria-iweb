package iwebpaqueteria.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HistoricoId implements Serializable {
    private Long envioId;
    private Long estadoId;

    public HistoricoId() {
    }

    public HistoricoId(Long envioId, Long estadoId) {
        this.envioId = envioId;
        this.estadoId = estadoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoId that = (HistoricoId) o;
        return Objects.equals(envioId, that.envioId) && Objects.equals(estadoId, that.estadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(envioId, estadoId);
    }
}
