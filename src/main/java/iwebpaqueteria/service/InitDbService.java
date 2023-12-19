package iwebpaqueteria.service;

import iwebpaqueteria.model.Rol;
import iwebpaqueteria.model.Usuario;
import iwebpaqueteria.repository.RolRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
// Se ejecuta solo si el perfil activo es 'dev'
@Profile("dev")
public class InitDbService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // Se ejecuta tras crear el contexto de la aplicación
    // para inicializar la base de datos
    @PostConstruct
    public void initDatabase() {
        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setContrasenya("123");
        usuario.setTelefono("123456789");
        usuarioRepository.save(usuario);

        Rol webmaster =  new Rol();
        webmaster.setNombre("webmaster");
        rolRepository.save(webmaster);

        Rol tienda =  new Rol();
        tienda.setNombre("tienda");
        rolRepository.save(tienda);

        Rol repartidor =  new Rol();
        repartidor.setNombre("repartidor");
        rolRepository.save(repartidor);

    }

}
