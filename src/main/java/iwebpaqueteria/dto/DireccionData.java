package iwebpaqueteria.dto;

import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.model.Usuario;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DireccionData {
        private Long id;
        @NotNull(message = "El código postal es un campo obligatorio")
        @NotEmpty(message = "El código postal no puede estar vacío")
        private String codigoPostal;
        @NotNull(message = "La localidad es un campo obligatorio")
        @NotEmpty(message = "La localidad no puede estar vacía")
        private String localidad;
        @NotNull(message = "La provincia es un campo obligatorio")
        @NotEmpty(message = "La provincia no puede estar vacía")
        private String provincia;
        @Positive(message = "El número es un campo obligatorio mayor que 0")
        private int numero;
        @Positive(message = "La planta es un campo obligatorio mayor que 0")
        private int planta = -1;
        @NotNull(message = "El nombre de calle es un campo obligatorio")
        private String calle;
        @NotNull(message = "El número de teléfono es un campo obligatorio")
        private String telefono;
        @NotNull(message = "El nombre es un campo obligatorio")
        private String nombre;

        private Long usuarioid;

        public DireccionData() {}

        public DireccionData(Direccion direccion){
            this.codigoPostal = direccion.getCodigoPostal();
            this.localidad = direccion.getLocalidad();
            this.provincia = direccion.getProvincia();
            this.numero = direccion.getNumero();
            this.planta = direccion.getPlanta();
            this.calle = direccion.getCalle();
            this.telefono = direccion.getTelefono();
            this.nombre = direccion.getNombre();
        }

        public DireccionData(String codigoPostal, String localidad, String provincia, int numero, int planta, String calle, String telefono, String nombre) {
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

        public Long getUsuarioid() {
            return usuarioid;
        }

        public void setUsuarioid(Long usuarioid) {
            this.usuarioid = usuarioid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DireccionData direccion = (DireccionData) o;
            return numero == direccion.numero && planta == direccion.planta && Objects.equals(id, direccion.id) && Objects.equals(codigoPostal, direccion.codigoPostal) && Objects.equals(localidad, direccion.localidad) && Objects.equals(provincia, direccion.provincia) && Objects.equals(calle, direccion.calle) && Objects.equals(telefono, direccion.telefono);
        }

        @Override
        public int hashCode() {
            return Objects.hash(codigoPostal, localidad, provincia, numero, planta, calle, telefono);
        }
}
