package iwebpaqueteria.service;

import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.EnvioReducidoData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.repository.EstadoRepository;
import iwebpaqueteria.repository.HistoricoRepository;
import iwebpaqueteria.util.InitDbUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean.sql")
public class EnvioServiceTest {
    @Autowired
    private EnvioService envioService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DireccionService direccionService;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private HistoricoRepository historicoRepository;
    @Autowired
    private InitDbUtil initDbUtil;
    @BeforeEach
    public void init(){
        initDbUtil.initRoles();
        initDbUtil.initEstados();
        initDbUtil.initTarifas();
    }

    private Map<String, Object> crearTiendaYDireccion(){
        UsuarioData tienda = new UsuarioData();

        tienda.setEmail("tienda@ua");
        tienda.setContrasenya("123");
        tienda.setNombre("123");
        tienda.setTelefono("123123123");

        DireccionData direccion = new DireccionData("03005", "Alicante", "Alicante", 1, 1, "Calle", "123123123", "Tienda");

        tienda = usuarioService.registrarTienda(tienda, direccion);

        DireccionData direccionDestino = new DireccionData("03010", "Alicante", "Alicante", 5, 7, "Otra", "456456456", "Destino");
        direccionDestino = direccionService.crearDireccion(direccionDestino);

        return Map.of("tienda", tienda, "direccionDestino", direccionDestino);
    }

    @Test
    public void registrarEnvioTest() {
        Map<String, Object> tiendaYDireccion = crearTiendaYDireccion();
        UsuarioData tienda = (UsuarioData) tiendaYDireccion.get("tienda");
        DireccionData direccionDestino = (DireccionData) tiendaYDireccion.get("direccionDestino");

        EnvioData envio = envioService.crearEnvio(50f, 1, "Ninguna observación", tienda.getId(), direccionDestino.getId());

        assertThat(envio.getBultos()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void cancelarEnvio(){
        Map<String, Object> tiendaYDireccion = crearTiendaYDireccion();
        UsuarioData tienda = (UsuarioData) tiendaYDireccion.get("tienda");
        DireccionData direccionDestino = (DireccionData) tiendaYDireccion.get("direccionDestino");

        EnvioData envio = envioService.crearEnvio(50f, 1, "Ninguna observación", tienda.getId(), direccionDestino.getId());

        envioService.cancelarEnvio(envio.getCodigo(), "Ya no lo necesito");

        envio = envioService.recuperarEnvio(envio.getId());

        assertThat(envio.getHistoricoIds()).hasSize(2);
    }

    @Test
    public void consultarEnvio(){
        Map<String, Object> tiendaYDireccion = crearTiendaYDireccion();
        UsuarioData tienda = (UsuarioData) tiendaYDireccion.get("tienda");
        DireccionData direccionDestino = (DireccionData) tiendaYDireccion.get("direccionDestino");

        EnvioData envio = envioService.crearEnvio(50f, 1, "Ninguna observación", tienda.getId(), direccionDestino.getId());

        envioService.cancelarEnvio(envio.getCodigo(), "Ya no lo necesito");

        EnvioReducidoData envioReducido = envioService.resumenEnvio(envio.getCodigo());

        assertThat(envioReducido).isNotNull();
        assertThat(envioReducido.getCodigo()).isEqualTo(envio.getCodigo());
        assertThat(envioReducido.getHistoricos()).hasSize(2);
        assertThat(envioReducido.getHistoricos().get(0).getEstado()).isEqualTo("En almacén");
    }
}
