package iwebpaqueteria.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND , reason="Envio no encontrado")
public class EnvioNotFoundException extends RuntimeException{
}
