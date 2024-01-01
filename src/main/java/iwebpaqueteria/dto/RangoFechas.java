package iwebpaqueteria.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class RangoFechas {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaInicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaFin;

    public RangoFechas() {
    }

    public RangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
