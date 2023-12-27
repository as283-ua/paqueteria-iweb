package iwebpaqueteria.repository;

import iwebpaqueteria.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String eMail);

    Optional<Usuario> findByNombre(String nombre);
}
