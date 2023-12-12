package iwebpaqueteria.dto;

import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.model.Rol;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class UsuarioData {

    private Long id;
    private String email;
    private String contrasenya;
    private String nombre;
    private String telefono;
    private String APIKey;
    private Direccion direccion;
    private Rol rol;
    private Set<Envio> envios;

// Getters y setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getContrasenya() { return contrasenya; }

    public void setContrasenya(String contrasenya) { this.contrasenya = contrasenya; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getAPIKey() { return APIKey; }

    public void setAPIKey(String APIKey) { this.APIKey = APIKey; }

    public Direccion getDireccion() { return direccion; }

    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public Rol getRol() { return rol; }

    public void setRol(Rol rol) { this.rol = rol; }

    public Set<Envio> getEnvios() { return envios; }

    public void setEnvios(Set<Envio> envios) { this.envios = envios; }

    // Sobreescribimos equals y hashCode para que dos usuarios sean iguales
    // si tienen el mismo ID (ignoramos el resto de atributos)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioData)) return false;
        UsuarioData that = (UsuarioData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}