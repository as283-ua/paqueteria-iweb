package iwebpaqueteria.service;

import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean.sql")
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void registrarTiendaTest() {
        UsuarioData tienda = new UsuarioData();

        tienda.setEmail("tienda@ua");
        tienda.setContrasenya("123");
        tienda.setNombre("123");
        tienda.setTelefono("123123123");

        tienda = usuarioService.registrarTienda(tienda);

        assertThat(tienda.getAPIKey()).isNotNull();
        assertThat(tienda.getAPIKey()).isNotBlank();
    }
}
