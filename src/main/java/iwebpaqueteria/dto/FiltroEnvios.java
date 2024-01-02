package iwebpaqueteria.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FiltroEnvios {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaInicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaFin;

    boolean hoy = false;

    public FiltroEnvios() {
    }

    public FiltroEnvios(boolean hoy) {
        this.hoy = hoy;
        fechaInicio = LocalDate.now().minusDays(1);
        fechaFin = LocalDate.now().plusDays(1);
    }

    public FiltroEnvios(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public FiltroEnvios(FiltroEnvios rangoFechas){
        this.fechaInicio = rangoFechas.getFechaInicio();
        this.fechaFin = rangoFechas.getFechaFin();
        this.hoy = rangoFechas.isHoy();
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


    public boolean isHoy() {
        return hoy;
    }

    public void setHoy(boolean hoy) {
        this.hoy = hoy;
    }
}
