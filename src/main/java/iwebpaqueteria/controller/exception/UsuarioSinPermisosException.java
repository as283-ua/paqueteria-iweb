package iwebpaqueteria.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="Usuario sin permisos para realizar esta accion")
public class UsuarioSinPermisosException extends RuntimeException{
}
