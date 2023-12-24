package iwebpaqueteria.dto;

import java.util.List;
import java.util.Objects;

public class TarifaData {
    private Long id;
    private String nombre;

    private float coste;

    private List<Long> enviosIds;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public float getCoste() { return coste; }

    public void setCoste(float coste) { this.coste = coste; }

    public List<Long> getEnviosIds() { return enviosIds; }

    public void setEnviosIds(List<Long> enviosIds) { this.enviosIds = enviosIds; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvioData)) return false;
        EnvioData that = (EnvioData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
