package iwebpaqueteria.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FiltroEnvios {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaInicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fechaFin;

    boolean hoy = false;

    boolean porEntregar = false;

    public FiltroEnvios() {
    }

    public FiltroEnvios(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public FiltroEnvios(FiltroEnvios rangoFechas){
        this.fechaInicio = rangoFechas.getFechaInicio();
        this.fechaFin = rangoFechas.getFechaFin();
        this.hoy = rangoFechas.isHoy();
        if(isHoy()){
            fechaInicio = LocalDate.now().minusDays(1);
            fechaFin = LocalDate.now().plusDays(1);
        }
        this.porEntregar = rangoFechas.isPorEntregar();
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

    public boolean isPorEntregar() {
        return porEntregar;
    }

    public void setPorEntregar(boolean porEntregar) {
        this.porEntregar = porEntregar;
    }

    public boolean vacio(){
        return !porEntregar &&
                !hoy &&
                fechaInicio == null &&
                fechaFin == null;
    }
}
