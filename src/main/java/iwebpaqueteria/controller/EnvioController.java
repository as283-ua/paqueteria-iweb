package iwebpaqueteria.controller;

import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.EnvioIncorrectoException;
import iwebpaqueteria.controller.exception.EnvioNotFoundException;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioDireccionData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.AsignarRepartidorData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.service.UsuarioService;
import iwebpaqueteria.service.EnvioService;
import iwebpaqueteria.service.exception.EnvioServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if(envio==null){
            throw new EnvioNotFoundException();
        }

        if (session.getAttribute("error")!=null) {
            model.addAttribute("error", session.getAttribute("error"));
            session.removeAttribute("error");
        }

        if(envio.getRepartidorId()==null)
            model.addAttribute("asignarRepartidor", new AsignarRepartidorData());
        else
            model.addAttribute("repartidor", usuarioService.findById(envio.getRepartidorId()).getNombre());

        model.addAttribute("usuario", usuario);
        model.addAttribute("envio", envio);
        model.addAttribute("direccionOrigen", direccionService.findById(envio.getDireccionOrigenId()));
        model.addAttribute("direccionDestino", direccionService.findById(envio.getDireccionDestinoId()));
        model.addAttribute("tarifas", envioService.tarifasDeEnvio(idEnvio));

        return "detalleEnvio";
    }

    @PostMapping("/envios/{id}/repartidor")
    public String asignarRepartidorEnvio(@PathVariable(value="id") Long idEnvio, @ModelAttribute AsignarRepartidorData asignarRepartidorData, Model model, HttpSession session){

        comprobarUsuarioLogeadoWebMaster();

        try{
            envioService.asignarRepartidor(idEnvio, asignarRepartidorData.getNombre());
        }
        catch (IllegalArgumentException e){
            if ("No existe un repartidor con ese nombre".equals(e.getMessage())){
                session.setAttribute("error", "No existe un repartidor con ese nombre");
            }
            else
                throw new EnvioNotFoundException();
        }


        return "redirect:/envios/" + idEnvio;
    }

    @GetMapping("/buscarRepartidores")
    @ResponseBody
    public List<String> buscarRepartidores(@RequestParam String nombre) {
        List <String> sugerencias =  usuarioService.buscarRepartidor(nombre);
        return sugerencias.stream()
                .limit(10)
                .map(s -> "<div class=\"sugerencia-item\">" + s + "</div>")
                .collect(Collectors.toList());
    }

}
