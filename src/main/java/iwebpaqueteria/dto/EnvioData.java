package iwebpaqueteria.dto;


import java.util.List;
import java.util.Objects;

public class EnvioData {

    private Long id;
    private Float peso;
    private Float precio;
    private String observaciones;
    private List<Long> tarifasIds;
    private Long direccionOrigenId;
    private Long direccionDestinoId;
    private Long repartidorId;
    private List<Long> historicosIds;

// Getters y setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Float getPeso() { return peso; }

    public void setPeso(Float peso) { this.peso = peso; }

    public Float getPrecio() { return precio; }

    public void setPrecio(Float precio) { this.precio = precio; }

    public String getObservaciones() { return observaciones; }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<Long> getTarifasIds() { return tarifasIds; }

    public void setTarifasIds(List<Long> tarifasIds) { this.tarifasIds = tarifasIds; }

    public Long getDireccionOrigenId() { return direccionOrigenId; }

    public void setDireccionOrigenId(Long direccionOrigenId) { this.direccionOrigenId = direccionOrigenId; }

    public Long getDireccionDestinoId() { return direccionDestinoId; }

    public void setDireccionDestinoId(Long direccionDestinoId) { this.direccionDestinoId = direccionDestinoId; }

    public Long getRepartidorId() { return repartidorId; }

    public void setRepartidorId(Long repartidorId) { this.repartidorId = repartidorId; }

    public List<Long> getHistoricosIds() { return historicosIds; }

    public void setHistoricosIds(List<Long> historicosIds) { this.historicosIds = historicosIds; }

    // Sobreescribimos equals y hashCode para que dos usuarios sean iguales
    // si tienen el mismo ID (ignoramos el resto de atributos)

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