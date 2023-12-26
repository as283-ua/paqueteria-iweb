package iwebpaqueteria.dto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class EnvioDireccionData {
    @NotNull(message = "El peso es un campo obligatorio")
    private float peso;
    private String observaciones;
    @NotNull(message = "El número de bultos es un campo obligatorio")
    @Min(value = 1, message = "El número de bultos debe ser mayor que 0")
    private int bultos;
    @NotNull(message = "La dirección de destino es un campo obligatorio")
    @Valid
    private DireccionData destino;

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getBultos() {
        return bultos;
    }

    public void setBultos(int bultos) {
        this.bultos = bultos;
    }

    public DireccionData getDestino() {
        return destino;
    }

    public void setDestino(DireccionData destino) {
        this.destino = destino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvioDireccionData that = (EnvioDireccionData) o;
        return Float.compare(peso, that.peso) == 0 && bultos == that.bultos && Objects.equals(observaciones, that.observaciones) && Objects.equals(destino, that.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peso, observaciones, bultos, destino);
    }
}
