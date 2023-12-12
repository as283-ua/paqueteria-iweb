package iwebpaqueteria.repository;

import iwebpaqueteria.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TestTodo {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    private DireccionRepository direccionRepository;

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

        Direccion direccion = new Direccion();
        direccion.setCodigoPostal("03002");
        direccion.setLocalidad("Alicante");
        direccion.setProvincia("Alicante");
        direccion.setNumero(1);
        direccion.setPlanta(1);
        direccion.setCalle("Calle");
        direccion.setTelefono("123456789");

        direccion = direccionRepository.save(direccion);


        Envio envio = new Envio();
        envio.setPeso(50);
        envio.setPrecio(10);
        envio.setObservaciones("Fragil");
        envio.setRepartidor(usuario);
        envio.setDireccionOrigen(direccion);
        envio.setDireccionDestino(direccion);

        envio = envioRepository.save(envio);

        Tarifa tarifa = new Tarifa();
        tarifa.setCoste(10);
        tarifa.setNombre("Larga distancia");
        tarifa = tarifaRepository.save(tarifa);


        assertThat(envio.getDireccionDestino().getCalle()).isEqualTo("Calle");

    }
}