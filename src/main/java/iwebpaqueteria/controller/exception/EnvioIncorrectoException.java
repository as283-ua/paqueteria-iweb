package iwebpaqueteria.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EnvioIncorrectoException extends RuntimeException{
    public EnvioIncorrectoException(String message) {
        super(message);
    }
}
