package iwebpaqueteria.service;

import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
// Se ejecuta solo si el perfil activo es 'dev'
@Profile("dev")
public class InitDbDevService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private void crearEstadoIfNotExists(String nombre){
        try{
            Estado estado = new Estado(nombre);
            estadoRepository.save(estado);
        } catch(Exception ignored){}
    }

    private void initEstados(){
        crearEstadoIfNotExists("En almacén");
        crearEstadoIfNotExists("Enviado");
        crearEstadoIfNotExists("En reparto");
        crearEstadoIfNotExists("Entregado");
        crearEstadoIfNotExists("Devuelto");
        crearEstadoIfNotExists("Cancelado");
    }

    // Se ejecuta tras crear el contexto de la aplicación
    // para inicializar la base de datos
    @PostConstruct
    public void initDatabase() {
        initEstados();

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
//        direccionOrigenTienda1 = direccionRepository.save(direccionOrigenTienda1);

        Usuario tienda1 = new Usuario("tienda1@ua");
        tienda1.setNombre("Tienda 1");
        tienda1.setContrasenya("123");
        tienda1.setTelefono("234567891");
        tienda1.setRol(tienda);
        tienda1.setAPIKey("123456789");
        tienda1.setDireccion(direccionOrigenTienda1);
        tienda1 = usuarioRepository.save(tienda1);

        Direccion direccionDestino = new Direccion("0300", "San Vicente", "Alicante", 2, 1, "Calle Vista", "0000000", "Juan Carlos");
        direccionRepository.save(direccionDestino);

        Tarifa tarifaCortaDistancia = new Tarifa("Corta distancia", 1);
        tarifaCortaDistancia = tarifaRepository.save(tarifaCortaDistancia);
        Tarifa tarifaLargaDistancia = new Tarifa("Larga distancia", 2);
        tarifaRepository.save(tarifaLargaDistancia);
        Tarifa tarifaBultos = new Tarifa("Bultos", 1);
        tarifaRepository.save(tarifaBultos);

        Envio envio = new Envio(1, 1, 1, "observaciones", direccionOrigenTienda1, direccionDestino);
        envio.addTarifa(tarifaCortaDistancia);
        envio.addTarifa(tarifaBultos);
        envioRepository.save(envio);
    }

}
