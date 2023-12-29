package iwebpaqueteria.dto;

import java.util.Date;
import java.util.Objects;

public class RangoFechas {
    Date fechaInicio;
    Date fechaFin;

    public RangoFechas() {
    }

    public RangoFechas(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
