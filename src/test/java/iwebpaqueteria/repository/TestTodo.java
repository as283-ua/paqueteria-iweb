package iwebpaqueteria.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class TestTodo {

    @Autowired
    private EnvioRepository envioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
}