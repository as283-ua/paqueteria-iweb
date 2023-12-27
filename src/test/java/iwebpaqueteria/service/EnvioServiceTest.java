package iwebpaqueteria.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "/clean.sql")
public class EnvioServiceTest {
    @Autowired
    private EnvioService envioService;

    @Test
    public void registrarEnvioTest() {
        // TODO
    }
}
