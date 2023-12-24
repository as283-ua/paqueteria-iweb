package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String codigoPostal;

    @NotNull
    private String localidad;

    @NotNull
    private String provincia;

    @NotNull
    private int numero;

    @NotNull
    private int planta;

    @NotNull
    private String calle;

    @NotNull
    private String telefono;

    @NotNull
    private String nombre;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "direccionOrigen")
    private Set<Envio> enviosOrigen = new HashSet<>();

    @OneToMany(mappedBy = "direccionDestino")
    private Set<Envio> enviosDestino = new HashSet<>();

    public Direccion() {}

    public Direccion(String codigoPostal, String localidad, String provincia, int numero, int planta, String calle, String telefono, String nombre) {
        this.codigoPostal = codigoPostal;
        this.localidad = localidad;
        this.provincia = provincia;
        this.numero = numero;
        this.planta = planta;
        this.calle = calle;
        this.telefono = telefono;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPlanta() {
        return planta;
    }

    public void setPlanta(int planta) {
        this.planta = planta;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Envio> getEnviosOrigen() {
        return enviosOrigen;
    }

    public Set<Envio> getEnviosDestino() {
        return enviosDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direccion direccion = (Direccion) o;
        return numero == direccion.numero && planta == direccion.planta && Objects.equals(id, direccion.id) && Objects.equals(codigoPostal, direccion.codigoPostal) && Objects.equals(localidad, direccion.localidad) && Objects.equals(provincia, direccion.provincia) && Objects.equals(calle, direccion.calle) && Objects.equals(telefono, direccion.telefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoPostal, localidad, provincia, numero, planta, calle, telefono);
    }
}
