package iwebpaqueteria.repository;

import iwebpaqueteria.model.Envio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EnvioRepository extends CrudRepository<Envio, Long> {
    Optional<Envio> findByCodigo(String codigo);
}
