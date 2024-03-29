package iwebpaqueteria.controller;

import iwebpaqueteria.controller.exception.EnvioIncorrectoException;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(apiKey == null){
            throw new UsuarioSinPermisosException();
        }

        UsuarioData usuario = usuarioService.findByAPIKey(apiKey);
        if (usuario == null) {
            throw new UsuarioSinPermisosException();
        }
        return usuario;
    }

    @PostMapping(value = "/envios", consumes = "application/json", produces = "application/json")
    public Map<String, Object> crearEnvio(@Valid @RequestBody EnvioDireccionData envioDireccionData, @RequestHeader(value = "Authorization", required = false) String apiKey) {
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
    public void cancelarEnvío(@PathVariable(value="codigo") String codigoEnvio, @RequestHeader(value = "Authorization", required = false) String apiKey, @RequestBody(required = false) Map<String, String> body) {
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
            throw new EnvioIncorrectoException(e.getMessage());
        }

        return envio;
    }

    @GetMapping(value = "/envios", produces = "application/json")
    public List<Map<String, Object>> enviosDeTienda(@RequestHeader(value = "Authorization", required = false) String apiKey, @RequestBody(required = false) FiltroEnvios rangoFechas) {
        UsuarioData tienda = validarApikey(apiKey);

        if (rangoFechas != null){
            if(rangoFechas.getFechaInicio() != null){
                rangoFechas.setFechaInicio(rangoFechas.getFechaInicio().minusDays(1));
            }
            if(rangoFechas.getFechaFin() != null){
                rangoFechas.setFechaFin(rangoFechas.getFechaFin().plusDays(1));
            }
        }

        return envioService.enviosReducidosTienda(tienda.getId(), rangoFechas);
    }

    @GetMapping(value = "/envios/tarifas", produces = "application/json")
    public Map<String, Object> consultarPrecio( @RequestParam(value = "peso") Integer peso,
                                                @RequestParam(value = "cp") String cp,
                                                @RequestParam(value = "bultos") Integer bultos,
                                                @RequestHeader(value = "Authorization", required = false) String apiKey) {
        Map<String, Object> response = new HashMap<>();
        UsuarioData tienda = validarApikey(apiKey);

        response.put("precio", envioService.calcularCoste(cp, bultos));
        response.put("tarifas", envioService.consultaTarifas(cp, bultos));
        return response;
    }
}
