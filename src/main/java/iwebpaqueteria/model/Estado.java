package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "estados")
public class Estado {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "estado")
    private Set<Historico> historicos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Historico> getHistoricos() {
        return historicos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estado estado = (Estado) o;
        return id == estado.id && Objects.equals(nombre, estado.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
