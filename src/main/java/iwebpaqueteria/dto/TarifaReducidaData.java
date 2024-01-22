package iwebpaqueteria.dto;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Objects;

public class TarifaReducidaData {
    private String nombre;

    private float coste;

    private int cantidad;

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public float getCoste() { return coste; }

    public void setCoste(float coste) { this.coste = coste; }

    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvioData)) return false;
        EnvioData that = (EnvioData) o;
        return Objects.equals(getNombre(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNombre());
    }
}
