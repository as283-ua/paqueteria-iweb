package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tarifas")
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique=true)
    private String nombre;

    @NotNull
    private float coste;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "envio_tarifa",
            joinColumns = { @JoinColumn(name = "fk_tarifa") },
            inverseJoinColumns = {@JoinColumn(name = "fk_envio")})
    private Set<Envio> envios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCoste() {
        return coste;
    }

    public void setCoste(float coste) {
        this.coste = coste;
    }

    public Set<Envio> getEnvios() {
        return envios;
    }

    public Tarifa() {}

    public Tarifa(String nombre, float coste) {
        this.nombre = nombre;
        this.coste = coste;
    }

    public void addEnvio(Envio envio) {
        if (envios.contains(envio)) return;
        // Añadimos la tarea a la lista
        envios.add(envio);
        // Establecemos la relación inversa del usuario en la tarea
        if (!envio.getTarifas().contains(this)) {
            envio.addTarifa(this);
        }
    }

    public void removeEnvio(Envio envio) {
        if (!envios.contains(envio)) return;
        // Eliminamos la tarea de la lista
        envios.remove(envio);
        // Eliminamos la relación inversa del usuario en la tarea
        if (envio.getTarifas().contains(this)) {
            envio.removeTarifa(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarifa tarifa = (Tarifa) o;
        return Float.compare(coste, tarifa.coste) == 0 && Objects.equals(id, tarifa.id) && Objects.equals(nombre, tarifa.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
