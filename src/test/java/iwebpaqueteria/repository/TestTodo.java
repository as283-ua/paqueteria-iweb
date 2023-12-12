package iwebpaqueteria.repository;

import iwebpaqueteria.model.Rol;
import iwebpaqueteria.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestTodo {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Test
    @Transactional
    public void absolutamenteTodoTest(){
        Rol rol = new Rol();
        rol.setNombre("Administrador");
        rol = rolRepository.save(rol);

        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setEmail("usu@ua");
        usuario.setContrasenya("1234");
        usuario.setTelefono("123456789");
        usuario.setRol(rol);

        usuario = usuarioRepository.save(usuario);

        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);

        assertThat(usuario).isNotNull();

        assertThat(usuario.getRol().getNombre()).isEqualTo("Administrador");
    }
}