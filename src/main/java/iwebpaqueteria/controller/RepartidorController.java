package iwebpaqueteria.controller;

import antlr.StringUtils;
import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.model.Usuario;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.service.EnvioService;
import iwebpaqueteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RepartidorController {

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    EnvioService envioService;

    @Autowired
    DireccionService direccionService;

    public Map<Long, DireccionData> direccionesDeEnvios(List<EnvioData> envios){
        Map<Long, DireccionData> result = new HashMap<>();

        for (EnvioData envio : envios) {
            result.put(envio.getId(), direccionService.findById(envio.getDireccionDestinoId()));
        }

        return result;
    }

    private void comprobarUsuarioLogeadoWebMaster() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
        if(!"webmaster".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre())){
            throw new UsuarioSinPermisosException();
        }
    }

    @GetMapping("/repartidores")
    public String listadoRepartidores(Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        model.addAttribute("repartidores", usuarioService.getRepartidoresRegistrados());

        return "listadoRepartidores";
    }

    @GetMapping("/repartidores/{id}")
    public String detalleRepartidor(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null)
            return "redirect:/repartidores";

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        model.addAttribute("repartidor", usuarioService.findById(idUsu));
        model.addAttribute("envios", envioService.getEnviosRepartidor(idUsu));
        model.addAttribute("direcciones", direccionesDeEnvios(envioService.getEnviosRepartidor(idUsu)));

        return "detalleRepartidor";
    }

    @GetMapping("/repartidores/nuevo")
    public String nuevoRepartidor(Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        if(model.getAttribute("repartidor") == null)
            model.addAttribute("repartidor", new UsuarioData());

        return "formNuevoRepartidor";
    }

    @PostMapping("/repartidores/nuevo")
    public String altaRepartidor(@ModelAttribute UsuarioData repartidor, Model model, RedirectAttributes flash) {

        comprobarUsuarioLogeadoWebMaster();

        if (!repartidor.getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}")){
            flash.addFlashAttribute("error", "El teléfono solo puede contener el código de país precedido de un simbolo + (opcional) y nueve números. Sin espacios");
            repartidor.setTelefono("");
            flash.addFlashAttribute("repartidor", repartidor);

            return "redirect:/repartidores/nuevo";
        }

        try{
            usuarioService.registrarRepartidor(repartidor);
        } catch(Exception e){
            flash.addFlashAttribute("error", e.getMessage());
            flash.addFlashAttribute("repartidor", repartidor);
            return "redirect:/repartidores/nuevo";
        }

        return "redirect:/repartidores";
    }

    @GetMapping("/repartidores/{id}/modificar")
    public String modificarRepartidor(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null){
            return "redirect:/repartidores";
        }

        UsuarioData repartidor = usuarioService.findById(idUsu);
        model.addAttribute("repartidor", repartidor);
        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));

        return "formModificarRepartidor";
    }

    @PostMapping("/repartidores/{id}/modificar")
    public String modRepartidor(@PathVariable(value="id") Long idUsu, @ModelAttribute UsuarioData repartidor, Model model, RedirectAttributes flash) {

        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null || !idUsu.equals(repartidor.getId())){
            return "redirect:/repartidores";
        }

        if (!repartidor.getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}")){
            repartidor.setTelefono("");
            flash.addFlashAttribute("repartidor", repartidor);
            flash.addFlashAttribute("error", "El teléfono solo puede contener el código de país precedido de un simbolo + (opcional) y nueve números. Sin espacios");
            return "redirect:/repartidores/" + idUsu + "/modificar";
        }

        try{
            usuarioService.modificarRepartidor(repartidor, idUsu);
        } catch (Exception e){
            flash.addFlashAttribute("repartidor", repartidor);
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/repartidores/" + idUsu + "/modificar";
        }

        return "redirect:/repartidores";
    }

    @DeleteMapping("/repartidores/{id}")
    public String borrarRepartidor(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        usuarioService.borrarUsuario(idUsu);

        return "redirect:/repartidores";
    }
}
