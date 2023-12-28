package iwebpaqueteria.dto;

public class HistoricoData {
    private String fecha;
    private String observaciones;
    private String estado;

    public HistoricoData() {}

    public HistoricoData(String fecha, String observaciones, String estado) {
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
