package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "direccion_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "direccionOrigen")
    private Set<Envio> enviosOrigen;

    @OneToMany(mappedBy = "direccionDestino")
    private Set<Envio> enviosDestino;

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
