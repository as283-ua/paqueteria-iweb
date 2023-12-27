package iwebpaqueteria.repository;

import iwebpaqueteria.model.Estado;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EstadoRepository extends CrudRepository<Estado, Long> {
    public Optional<Estado> findByNombre(String nombre);
}
