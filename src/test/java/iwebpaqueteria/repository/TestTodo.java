package iwebpaqueteria.repository;

import iwebpaqueteria.model.*;
import iwebpaqueteria.util.InitDbUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = "/clean.sql")
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

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    private InitDbUtil initDbUtil;

    @Test
    @Transactional
    public void absolutamenteTodoTest(){
        Rol rol = new Rol();
        rol.setNombre("Administrador");
        rol = rolRepository.save(rol);

        // Crear un usuario
        Usuario usuario = new Usuario();
        usuario.setEmail("usu@ua");
        usuario.setContrasenya("1234");
        usuario.setNombre("Juan");
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
        direccion.setNombre("a");

        direccion = direccionRepository.save(direccion);


        Envio envio = new Envio();
        envio.setPeso(50);
        envio.setPrecio(10);
        envio.setObservaciones("Fragil");
        envio.setRepartidor(usuario);
        envio.setDireccionOrigen(direccion);
        envio.setDireccionDestino(direccion);

        envio = envioRepository.save(envio);

        assertThat(envio.getDireccionDestino().getCalle()).isEqualTo("Calle");

        Tarifa tarifa = new Tarifa();
        tarifa.setCoste(10);
        tarifa.setNombre("Larga distancia");
        tarifa = tarifaRepository.save(tarifa);

        envio.getTarifas().add(tarifa);
        tarifa.getEnvios().add(envio);

        envio = envioRepository.save(envio);
        tarifa = tarifaRepository.save(tarifa);

        assertThat(envio.getTarifas().size()).isEqualTo(1);
        assertThat(tarifa.getEnvios().size()).isEqualTo(1);

        assertThat(envio.getTarifas().iterator().next().getId()).isEqualTo(tarifa.getId());
        assertThat(tarifa.getEnvios().iterator().next().getId()).isEqualTo(envio.getId());

    }

    @Test
    public void direccionDeUsuario(){
        Direccion direccion = new Direccion();
        direccion.setCodigoPostal("03002");
        direccion.setLocalidad("Alicante");
        direccion.setProvincia("Alicante");
        direccion.setNumero(1);
        direccion.setPlanta(1);
        direccion.setCalle("Calle");
        direccion.setTelefono("123456789");
        direccion.setNombre("a");

        Rol rol = new Rol();
        rol.setNombre("Tienda");
        rol = rolRepository.save(rol);

        // Crear un usuario
        Usuario tienda = new Usuario();
        tienda.setEmail("usu@ua");
        tienda.setContrasenya("1234");
        tienda.setNombre("Juan");
        tienda.setTelefono("123456789");
        tienda.setRol(rol);
        tienda.setDireccion(direccion);
        tienda = usuarioRepository.save(tienda);

        tienda = usuarioRepository.findById(tienda.getId()).orElse(null);
        assertThat(tienda).isNotNull();
        assertThat(tienda.getDireccion()).isNotNull();

        assertThat(tienda.getDireccion().getCalle()).isEqualTo(direccion.getCalle());
    }

    @Test
    @Transactional
    public void testRolUsuarios(){
        initDbUtil.initRoles();

        Rol rol = rolRepository.findByNombre("webmaster").orElse(null);
        assertThat(rol).isNotNull();

        Usuario usuario = new Usuario();
        usuario.setEmail("usu@ua");
        usuario.setContrasenya("1234");
        usuario.setNombre("Juan");
        usuario.setTelefono("123456789");
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        assertThat(rol.getUsuarios().size()).isEqualTo(1);
    }
}