package iwebpaqueteria.repository;

import iwebpaqueteria.model.Historico;
import iwebpaqueteria.model.HistoricoId;
import org.springframework.data.repository.CrudRepository;

public interface HistoricoRepository extends CrudRepository<Historico, HistoricoId> {
}
