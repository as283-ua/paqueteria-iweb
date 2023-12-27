package iwebpaqueteria.service;

import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Profile("!dev")
public class InitDbEssentialService {
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private void crearRolIfNotExists(String nombre){
        try{
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        } catch(Exception ignored){}
    }

    private void crearTarifaIfNotExists(String nombre, int coste){
        try{
            Tarifa tarifa = new Tarifa(nombre, coste);
            tarifaRepository.save(tarifa);
        } catch(Exception ignored){}
    }

    private void crearEstadoIfNotExists(String nombre){
        try{
            Estado estado = new Estado(nombre);
            estadoRepository.save(estado);
        } catch(Exception ignored){}
    }

    private void initRoles(){
        crearRolIfNotExists("webmaster");
        crearRolIfNotExists("tienda");
        crearRolIfNotExists("repartidor");
    }

    private void initTarifas(){
        crearTarifaIfNotExists("Corta distancia", 1);
        crearTarifaIfNotExists("Larga distancia", 2);
        crearTarifaIfNotExists("Bultos", 1);
    }

    public void initEstados(){
        crearEstadoIfNotExists("En almacén");
        crearEstadoIfNotExists("Enviado");
        crearEstadoIfNotExists("En reparto");
        crearEstadoIfNotExists("Entregado");
        crearEstadoIfNotExists("Devuelto");
        crearEstadoIfNotExists("Cancelado");
    }

    @PostConstruct
    @Transactional
    public void initDatabase() {
        initRoles();

        initTarifas();

        initEstados();
    }

}
