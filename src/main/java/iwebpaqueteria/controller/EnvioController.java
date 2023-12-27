package iwebpaqueteria.controller;

import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.EnvioIncorrectoException;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioDireccionData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.AsignarRepartidorData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.service.UsuarioService;
import iwebpaqueteria.service.EnvioService;
import iwebpaqueteria.service.exception.EnvioServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EnvioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EnvioService envioService;

    @Autowired
    DireccionService direccionService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
    }

    private void comprobarUsuarioLogeadoWebMaster() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
        if(!"webmaster".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre())){
            throw new UsuarioSinPermisosException();
        }
    }

    @GetMapping("/")
    public String about() {
        return "buscarEnvio";
    }

    @GetMapping("/envios")
    public String listadoEnvios(Model model, HttpSession session) {

        comprobarUsuarioLogeado();

        UsuarioData usuario = usuarioService.findById(managerUserSession.usuarioLogeado());

        model.addAttribute("usuario", usuario);
        model.addAttribute("envios", envioService.findAll());
        Float precioTotal = envioService.calcularPrecioTotal(envioService.findAll());
        model.addAttribute("precioTotal", precioTotal);
        return "listadoEnvios";
    }

    @GetMapping("/envios/{id}")
    public String detalleEnvio(@PathVariable(value="id") Long idEnvio, Model model, HttpSession session) {

        comprobarUsuarioLogeado();

        UsuarioData usuario = usuarioService.findById(managerUserSession.usuarioLogeado());

        EnvioData envio = envioService.recuperarEnvio(idEnvio);

        if(envio.getRepartidorId()==null)
            model.addAttribute("asignarRepartidor", new AsignarRepartidorData());
        else
            model.addAttribute("repartidor", usuarioService.findById(envio.getRepartidorId()).getNombre());

        model.addAttribute("usuario", usuario);
        model.addAttribute("envio", envio);
        model.addAttribute("tarifas", envioService.tarifasDeEnvio(idEnvio));

        return "detalleEnvio";
    }

    @PostMapping(value = "/envios", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map<String, Object> crearEnvio(@Valid @RequestBody EnvioDireccionData envioDireccionData, @RequestHeader("Authorization") String apiKey) {
        Map<String, Object> response = new HashMap<>();

        UsuarioData tienda= usuarioService.findByAPIKey(apiKey);
        if (tienda == null) {
            response.put("error", "API Key no válida");
            return response;
        }

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

    @PostMapping("/envios/{id}/repartidor")
    public String asignarRepartidorEnvio(@PathVariable(value="id") Long idEnvio, @ModelAttribute AsignarRepartidorData asignarRepartidorData, Model model, HttpSession session){

        comprobarUsuarioLogeadoWebMaster();

        envioService.asignarRepartidor(idEnvio, asignarRepartidorData.getNombre());

        return "redirect:/envios/" + idEnvio;
    }
}
