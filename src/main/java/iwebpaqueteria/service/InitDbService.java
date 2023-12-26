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
public class InitDbService {

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

        usuario.setRol(webmaster);
        usuarioRepository.save(usuario);

        Usuario tienda1 = new Usuario("tienda1@ua");
        tienda1.setNombre("Tienda 1");
        tienda1.setContrasenya("123");
        tienda1.setTelefono("234567891");
        tienda1.setRol(tienda);
        usuarioRepository.save(tienda1);

        Usuario repartidor1 = new Usuario("repartidor1@ua");
        repartidor1.setNombre("Repartidor 1");
        repartidor1.setContrasenya("123");
        repartidor1.setTelefono("345678912");
        repartidor1.setRol(repartidor);
        usuarioRepository.save(repartidor1);

        Direccion direccionOrigenTienda1 = new Direccion("0300", "San Vicente", "Alicante", 1, 0, "Calle Alberto", "234567891", "Tienda 1");
        direccionRepository.save(direccionOrigenTienda1);

        Direccion direccionDestino = new Direccion("0300", "San Vicente", "Alicante", 2, 1, "Calle Vista", "0000000", "Juan Carlos");
        direccionRepository.save(direccionDestino);

        Tarifa tarifaCortaDistancia = new Tarifa("Corta Distancia", 1);
        tarifaRepository.save(tarifaCortaDistancia);
        Tarifa tarifaLargaDistancia = new Tarifa("Larga Distancia", 2);
        tarifaRepository.save(tarifaLargaDistancia);
        Tarifa tarifaBultos = new Tarifa("Bultos", 1);
        tarifaRepository.save(tarifaBultos);

        Envio envio = new Envio(1, 1, 1, "observaciones", direccionOrigenTienda1, direccionDestino);
        envio.addTarifa(tarifaCortaDistancia);
        envio.addTarifa(tarifaBultos);
        envio.setRepartidor(repartidor1);
        envioRepository.save(envio);
    }

}
