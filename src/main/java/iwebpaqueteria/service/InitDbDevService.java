package iwebpaqueteria.service;

import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.*;
import iwebpaqueteria.util.InitDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
// Se ejecuta solo si el perfil activo es 'dev'
@Profile("dev")
public class InitDbDevService {

    @Autowired
    private InitDbUtil initDbUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private EnvioService envioService;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    @PostConstruct
    public void initDatabase() {
        Map<String, List<?>> result = initDbUtil.initDatabase();

        List<Rol> roles = (List<Rol>) result.get("roles");
        List<Tarifa> tarifas = (List<Tarifa>) result.get("tarifas");
        List<Estado> estados = (List<Estado>) result.get("estados");

        Rol webmaster =  roles.get(0);
        Rol tienda =  roles.get(1);
        Rol repartidor =  roles.get(2);

        Tarifa tarifaCortaDistancia = tarifas.get(0);
        Tarifa tarifaLargaDistancia = tarifas.get(1);
        Tarifa tarifaBultos = tarifas.get(2);

        Estado enAlmacen = estados.get(0);
        Estado recogido = estados.get(1);
        Estado ausente = estados.get(2);
        Estado enReparto = estados.get(3);
        Estado entregado = estados.get(4);
        Estado devuelto = estados.get(5);
        Estado cancelado = estados.get(6);

        Usuario usuario = new Usuario("user@ua");
        usuario.setNombre("Usuario Ejemplo");
        usuario.setContrasenya("123");
        usuario.setTelefono("123456789");
        usuarioRepository.save(usuario);

        usuario.setRol(webmaster);
        usuarioRepository.save(usuario);

        for(int i=0; i<15; i++) {
            Usuario rep = new Usuario("repartidor" + i + "@ua");
            rep.setNombre("Repartidor " + i);
            rep.setContrasenya("123");
            rep.setTelefono("345678912" + i);
            rep.setRol(repartidor);
            usuarioRepository.save(rep);
        }

        Direccion direccionOrigenTienda1 = new Direccion("0300", "San Vicente", "Alicante", 1, 0, "Calle Alberto", "234567891", "Tienda 1");
        Direccion direccionOrigenTienda2 = new Direccion("0300", "Alicante", "Alicante", 17, 0, "Calle Tienda 2", "123123112", "Tienda 2");


        Usuario tienda1 = new Usuario("tienda1@ua");
        tienda1.setNombre("Tienda 1");
        tienda1.setContrasenya("123");
        tienda1.setTelefono("234567891");
        tienda1.setRol(tienda);
        tienda1.setAPIKey("123456789");
        tienda1.setDireccion(direccionOrigenTienda1);
        tienda1 = usuarioRepository.save(tienda1);

        Usuario tienda2 = new Usuario("tienda2@ua");
        tienda2.setNombre("Tienda 2");
        tienda2.setContrasenya("123");
        tienda2.setTelefono("2345678914");
        tienda2.setRol(tienda);
        tienda2.setAPIKey("1234567890");
        tienda2.setDireccion(direccionOrigenTienda2);
        tienda2 = usuarioRepository.save(tienda2);

        Direccion direccionDestino = new Direccion("0300", "San Vicente", "Alicante", 2, 1, "Calle Vista", "0000000", "Juan Carlos");
        direccionRepository.save(direccionDestino);

        envioService.crearEnvio(1, 1,"observaciones", tienda1.getId(), direccionDestino.getId());

        envioService.crearEnvio(1, 1,"observaciones envio 2", tienda1.getId(), direccionDestino.getId());
    }

}
