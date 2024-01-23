package iwebpaqueteria.util;

import iwebpaqueteria.model.Estado;
import iwebpaqueteria.model.Rol;
import iwebpaqueteria.model.Tarifa;
import iwebpaqueteria.repository.EstadoRepository;
import iwebpaqueteria.repository.RolRepository;
import iwebpaqueteria.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.swing.text.TableView;
import java.util.*;

@Component
public class InitDbUtil {
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    @Transactional
    public Rol crearRolIfNotExists(String nombre){
        try{
            Rol rol = new Rol();
            rol.setNombre(nombre);
            return rolRepository.save(rol);
        } catch(Exception ignored){}
        return rolRepository.findByNombre(nombre).orElse(null);
    }

    @Transactional
    public Tarifa crearTarifaIfNotExists(String nombre, int coste){
        try{
            Tarifa tarifa = new Tarifa(nombre, coste);
            return tarifaRepository.save(tarifa);
        } catch(Exception ignored){}
        return tarifaRepository.findByNombre(nombre).orElse(null);
    }

    @Transactional
    public Estado crearEstadoIfNotExists(String nombre){
        try{
            Estado estado = new Estado(nombre);
            return estadoRepository.save(estado);
        } catch(Exception ignored){}
        return null;
    }

    public List<Rol> initRoles(){
        List<Rol> roles = new ArrayList<>();
        roles.add(crearRolIfNotExists("webmaster"));
        roles.add(crearRolIfNotExists("tienda"));
        roles.add(crearRolIfNotExists("repartidor"));
        return roles;
    }

    public List<Tarifa> initTarifas(){
        List<Tarifa> tarifas = new ArrayList<>();
        tarifas.add(crearTarifaIfNotExists("Corta distancia", 5));
        tarifas.add(crearTarifaIfNotExists("Larga distancia", 10));
        tarifas.add(crearTarifaIfNotExists("Bultos", 3));
        return tarifas;
    }

    public List<Estado> initEstados(){
        List<Estado> estados = new ArrayList<>();
        estados.add(crearEstadoIfNotExists("En almacén"));
        estados.add(crearEstadoIfNotExists("Recogido por repartidor"));
        estados.add(crearEstadoIfNotExists("Ausente"));
        estados.add(crearEstadoIfNotExists("En reparto"));
        estados.add(crearEstadoIfNotExists("Entregado"));
        estados.add(crearEstadoIfNotExists("Rechazado"));
        estados.add(crearEstadoIfNotExists("Cancelado"));
        return estados;
    }

    public Map<String, List<?>> initDatabase() {
        Map<String, List<?>> result = new HashMap<>();
        result.put("roles", initRoles());
        result.put("estados", initEstados());
        result.put("tarifas", initTarifas());
        return result;
    }

}