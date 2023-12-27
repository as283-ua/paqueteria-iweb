package iwebpaqueteria.util;

import iwebpaqueteria.model.Estado;
import iwebpaqueteria.model.Rol;
import iwebpaqueteria.model.Tarifa;
import iwebpaqueteria.repository.EstadoRepository;
import iwebpaqueteria.repository.RolRepository;
import iwebpaqueteria.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class InitDbUtil {
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

    public void initRoles(){
        crearRolIfNotExists("webmaster");
        crearRolIfNotExists("tienda");
        crearRolIfNotExists("repartidor");
    }

    public void initTarifas(){
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
