package iwebpaqueteria.dto;

import java.util.List;
import java.util.Objects;

public class EnvioReducidoData {
    private String codigo;
    private float peso;
    private int bultos;
    private List<HistoricoData> historicos;

    public EnvioReducidoData() {}

    public EnvioReducidoData(String codigo, float peso, int bultos, List<HistoricoData> historicos) {
        this.codigo = codigo;
        this.peso = peso;
        this.bultos = bultos;
        this.historicos = historicos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public int getBultos() {
        return bultos;
    }

    public void setBultos(int bultos) {
        this.bultos = bultos;
    }

    public List<HistoricoData> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<HistoricoData> historicos) {
        this.historicos = historicos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvioReducidoData that = (EnvioReducidoData) o;
        return Objects.equals(codigo, that.codigo) && Objects.equals(peso, that.peso) && Objects.equals(bultos, that.bultos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, peso, bultos);
    }
}
