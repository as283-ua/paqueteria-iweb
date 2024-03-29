package iwebpaqueteria.repository;

import iwebpaqueteria.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String eMail);
    Optional<Usuario> findByNombre(String nombre);
    Optional<List<Usuario>> findAllByNombreIgnoreCaseStartsWith(String nombre);
    Optional<Usuario> findByAPIKey(String apiKey);
    Optional<Usuario> findByTelefono(String telefono);
}
