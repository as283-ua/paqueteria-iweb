package iwebpaqueteria.controller;

import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.dto.EnvioDireccionData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.service.UsuarioService;
import iwebpaqueteria.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class EnvioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EnvioService envioService;

    @Autowired
    ManagerUserSession managerUserSession;

    private void comprobarUsuarioLogeado() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
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

        model.addAttribute("usuario", usuario);
        model.addAttribute("envio", envioService.recuperarEnvio(idEnvio));
        model.addAttribute("tarifas", envioService.tarifasDeEnvio(idEnvio));

        return "detalleEnvio";
    }

    @PostMapping(value = "/envios", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String crearEnvio(@Valid @RequestBody EnvioDireccionData envioDireccionData) {
        return "OK";
    }
}
