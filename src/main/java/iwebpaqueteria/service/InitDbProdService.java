package iwebpaqueteria.service;

import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.UsuarioRepository;
import iwebpaqueteria.util.InitDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Profile("!dev")
public class InitDbProdService {
    @Autowired
    private InitDbUtil initDbUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    @Transactional
    public void initDatabase() {
        Map<String, List<?>> result = initDbUtil.initDatabase();

        List<Rol> roles = (List<Rol>) result.get("roles");

        Rol webmaster =  roles.get(0);

        Usuario usuario = new Usuario("admin@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setContrasenya("admin123");
        usuario.setTelefono("123456789");
        usuario.setRol(webmaster);
        try{
            usuarioRepository.save(usuario);
        } catch (Exception ignored){}
    }

}
