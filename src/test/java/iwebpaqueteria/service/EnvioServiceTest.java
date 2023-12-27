package iwebpaqueteria.service;

import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.model.Estado;
import iwebpaqueteria.model.HistoricoId;
import iwebpaqueteria.repository.EstadoRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import iwebpaqueteria.util.InitDbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
    private InitDbUtil initDbUtil;
    @BeforeEach
    public void init(){
        initDbUtil.initRoles();
        initDbUtil.initEstados();
        initDbUtil.initTarifas();
    }

    @Test
    public void registrarEnvioTest() {
        UsuarioData tienda = new UsuarioData();

        tienda.setEmail("tienda@ua");
        tienda.setContrasenya("123");
        tienda.setNombre("123");
        tienda.setTelefono("123123123");

        DireccionData direccion = new DireccionData("03005", "Alicante", "Alicante", 1, 1, "Calle", "123123123", "Tienda");

        tienda = usuarioService.registrarTienda(tienda, direccion);

        DireccionData direccionDestino = new DireccionData("03010", "Alicante", "Alicante", 5, 7, "Otra", "456456456", "Destino");
        direccionDestino = direccionService.crearDireccion(direccionDestino);

        EnvioData envio = envioService.crearEnvio(50f, 1, "Ninguna observación", tienda.getId(), direccionDestino.getId());

        assertThat(envio.getBultos()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void cancelarEnvio(){
        UsuarioData tienda = new UsuarioData();

        tienda.setEmail("tienda@ua");
        tienda.setContrasenya("123");
        tienda.setNombre("123");
        tienda.setTelefono("123123123");

        DireccionData direccion = new DireccionData("03005", "Alicante", "Alicante", 1, 1, "Calle", "123123123", "Tienda");

        tienda = usuarioService.registrarTienda(tienda, direccion);

        DireccionData direccionDestino = new DireccionData("03010", "Alicante", "Alicante", 5, 7, "Otra", "456456456", "Destino");
        direccionDestino = direccionService.crearDireccion(direccionDestino);

        EnvioData envio = envioService.crearEnvio(50f, 1, "Ninguna observación", tienda.getId(), direccionDestino.getId());

        envioService.cancelarEnvio(envio.getCodigo());

        Estado cancelado = estadoRepository.findByNombre("Cancelado").orElse(null);

        assertThat(cancelado).isNotNull();

        assertThat(envio.getHistoricosIds()).contains(new HistoricoId(envio.getId(), cancelado.getId()));
    }
}
