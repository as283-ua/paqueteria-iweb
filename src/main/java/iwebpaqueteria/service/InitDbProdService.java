package iwebpaqueteria.service;

import com.github.javafaker.Faker;
import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.DireccionRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import iwebpaqueteria.util.InitDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
@Profile("!dev")
public class InitDbProdService {
    @Autowired
    private InitDbUtil initDbUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EnvioService envioService;
    @Autowired
    private DireccionRepository direccionRepository;

    private void creacionEnvios(Long[] tiendas, Faker faker){
        for (int i = 0; i < 10; i++) {
            Direccion d = new Direccion(faker.address().zipCode(), faker.address().city(), faker.address().state(), faker.number().numberBetween(1, 100), faker.number().numberBetween(0, 1), faker.address().streetName(), faker.phoneNumber().cellPhone(), faker.name().fullName());
            direccionRepository.save(d);
            envioService.crearEnvio(faker.number().numberBetween(1, 5), faker.number().numberBetween(1, 5), faker.lorem().sentence(), tiendas[faker.number().numberBetween(0, 5)], d.getId());
        }
    }

    @PostConstruct
    @Transactional
    public void initDatabase() {
        Map<String, List<?>> result = initDbUtil.initDatabase();
        //Array para guardar los ids de las tiendas
        Long[] tiendas = new Long[5];

        Faker faker = new Faker(new Locale("es"));

        if (usuarioRepository.count() > 1) {
            /* Descomentar si se quiere que se creen envios cada vez que se inicie la aplicacion
            Iterable<Usuario> usuarios = usuarioRepository.findAll();
            int i = 0;
            for (Usuario u : usuarios) {
                if (u.getRol().getNombre().equals("tienda") && !Objects.equals(u.getNombre(), "Tienda Ejemplo")) {
                    tiendas[i] = u.getId();
                    i++;
                }
            }

            creacionEnvios(tiendas, faker);
             */
            return;
        }

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

        // Creando 1 repartidor conocido
        Usuario usu = new Usuario("repartidor@ua");
        usu.setNombre("Repartidor Ejemplo");
        usu.setContrasenya("123");
        usu.setTelefono("234567891");
        usu.setRol(roles.get(2));
        try{
            usuarioRepository.save(usu);
        } catch (Exception ignored){}

        // Creando 20 repartidores
        for (int i = 0; i < 20; i++) {
            Usuario u = new Usuario(faker.internet().emailAddress());
            u.setNombre(faker.name().fullName());
            u.setContrasenya(faker.internet().password());
            u.setTelefono(faker.phoneNumber().subscriberNumber(9));
            u.setRol(roles.get(2));
            try{
                usuarioRepository.save(u);
            } catch (Exception ignored){}
        }

        // Creando 1 tienda conocida
        Usuario tienda = new Usuario("tienda@ua");
        tienda.setNombre("Tienda Ejemplo");
        tienda.setContrasenya("123");
        tienda.setTelefono("345678912");
        tienda.setRol(roles.get(1));
        tienda.setAPIKey(faker.internet().password(15,18));
        Direccion direccionTienda = new Direccion("03000", "San Vicente", "Alicante", 1, 1, "Calle Silvestre", "234567891", "Juan Martinez");
        direccionRepository.save(direccionTienda);
        tienda.setDireccion(direccionTienda);
        try{
            usuarioRepository.save(tienda);
        } catch (Exception ignored){}

        // Creando 1 direccion destino conocida
        Direccion direccionDestino = new Direccion("03000", "San Vicente", "Alicante", 1, 1, "Calle Marquez", "622201009", "Antonio Corola");
        direccionRepository.save(direccionDestino);

        // Creando 5 tiendas
        for (int i = 0; i < 5; i++) {
            Usuario u = new Usuario(faker.internet().emailAddress());
            u.setNombre(faker.company().name());
            u.setContrasenya(faker.internet().password());
            u.setTelefono(faker.phoneNumber().subscriberNumber(9));
            u.setRol(roles.get(1));
            u.setAPIKey(faker.internet().password(15,18));
            Direccion d = new Direccion(faker.address().zipCode(), faker.address().city(), faker.address().state(), faker.number().numberBetween(1, 100), faker.number().numberBetween(0, 3), faker.address().streetName(), faker.phoneNumber().subscriberNumber(9), faker.name().fullName());
            direccionRepository.save(d);
            u.setDireccion(d);
            try{
                usuarioRepository.save(u);
                tiendas[i] = u.getId();
            } catch (Exception ignored){}
        }

        // Creando 1 envio conocido
        envioService.crearEnvio(1, 1,"Si no hay nadie en casa, dejar paquete al vecino.", tienda.getId(), direccionDestino.getId());
        // Creando 10 envios
        creacionEnvios(tiendas, faker);
    }

}
