package iwebpaqueteria.controller;

import iwebpaqueteria.controller.exception.EnvioIncorrectoException;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.dto.*;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.service.EnvioService;
import iwebpaqueteria.service.UsuarioService;
import iwebpaqueteria.service.exception.EnvioServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EnvioAPIController {
    Logger logger = LoggerFactory.getLogger(EnvioService.class);

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EnvioService envioService;

    @Autowired
    DireccionService direccionService;

    private UsuarioData validarApikey(String apiKey){
        UsuarioData usuario = usuarioService.findByAPIKey(apiKey);
        if (usuario == null) {
            throw new UsuarioNoLogeadoException();
        }
        return usuario;
    }

    @PostMapping(value = "/envios", consumes = "application/json", produces = "application/json")
    public Map<String, Object> crearEnvio(@Valid @RequestBody EnvioDireccionData envioDireccionData, @RequestHeader("Authorization") String apiKey) {
        logger.debug("POST /envios");
        Map<String, Object> response = new HashMap<>();

        UsuarioData tienda = validarApikey(apiKey);

        DireccionData destino = direccionService.crearDireccion(envioDireccionData.getDestino());
        EnvioData envio;
        try{
            envio = envioService.crearEnvio(envioDireccionData.getPeso(), envioDireccionData.getBultos(), envioDireccionData.getObservaciones(), tienda.getId(), destino.getId());
        } catch (EnvioServiceException e){
            throw new EnvioIncorrectoException(e.getMessage());
        }

        response.put("codigo", envio.getCodigo());
        return response;
    }

    @PostMapping(value = "/envios/{codigo}/historico/cancelar", consumes = "application/json", produces = "application/json")
    public void cancelarEnvío(@PathVariable(value="codigo") String codigoEnvio, @RequestHeader("Authorization") String apiKey, @RequestBody Map<String, String> body) {
        UsuarioData tienda = validarApikey(apiKey);

        String observaciones = body.get("observaciones");

        try{
            envioService.cancelarEnvio(codigoEnvio, observaciones);
        } catch (EnvioServiceException e){
            throw new EnvioIncorrectoException(e.getMessage());
        }
    }

    @GetMapping(value = "/envios/{codigo}", produces = "application/json")
    public EnvioReducidoData recuperarEnvio(@PathVariable(value="codigo") String codigoEnvio) {
        EnvioReducidoData envio;

        try{
            envio = envioService.resumenEnvio(codigoEnvio);
        } catch (EnvioServiceException e) {
            throw new EnvioIncorrectoException("Envío con código " + codigoEnvio + " no existe");
        }

        return envio;
    }
}