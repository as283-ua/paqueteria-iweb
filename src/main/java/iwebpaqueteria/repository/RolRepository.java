package iwebpaqueteria.repository;

import iwebpaqueteria.model.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolRepository extends CrudRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
