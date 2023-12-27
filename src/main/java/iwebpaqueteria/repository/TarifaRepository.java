package iwebpaqueteria.repository;

import iwebpaqueteria.model.Tarifa;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TarifaRepository extends CrudRepository<Tarifa, Long> {
    Optional<Tarifa> findByNombre(String largaDistancia);
}
