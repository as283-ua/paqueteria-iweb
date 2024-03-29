package iwebpaqueteria.controller;
import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.service.EnvioService;
import iwebpaqueteria.service.RolService;
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
import java.util.Set;

@Controller
public class TiendaController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;
    @Autowired
    RolService rolService;
    @Autowired
    EnvioService envioService;
    @Autowired
    DireccionService direccionService;
    @GetMapping("/tiendas/{userId}/APIKEY")
    public String mostrarApiKey(@PathVariable(value = "userId") Long idTienda, Model model, HttpSession session) {
        comprobarUsuarioLogeado();

        Long idYo = managerUserSession.usuarioLogeado();
        if (!idYo.equals(idTienda)) {
            throw new UsuarioSinPermisosException();
        }

        UsuarioData tienda = usuarioService.findById(idTienda);
        model.addAttribute("usuario", tienda);
        model.addAttribute("tienda", tienda);

        return "APIKEY";
    }

    @PostMapping("/tiendas/{userId}/APIKEY")
    public String cambiarApiKey(@PathVariable(value = "userId") Long idTienda, HttpSession session){
        comprobarUsuarioLogeado();

        Long idYo = managerUserSession.usuarioLogeado();
        if (!idYo.equals(idTienda)) {
            throw new UsuarioSinPermisosException();
        }

        usuarioService.cambiarApiKey(idTienda);

        return "redirect:/tiendas/"+ idYo +"/APIKEY";
    }

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

    @GetMapping("/tiendas")
    public String tiendasWebmaster(Model model, HttpSession session){
        comprobarUsuarioLogeadoWebMaster();

        Set<UsuarioData> tiendas = rolService.listarUsuariosPorRol("tienda");
        model.addAttribute("tiendas", tiendas);
        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));

        return "listadoTiendas";
    }

    @DeleteMapping("/tiendas/{id}")
    public String borrarTienda(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        usuarioService.borrarUsuario(idUsu);

        return "redirect:/tiendas";
    }

    public Map<Long, DireccionData> direccionesDeEnvios(List<EnvioData> envios){
        Map<Long, DireccionData> result = new HashMap<>();

        for (EnvioData envio : envios) {
            result.put(envio.getId(), direccionService.findById(envio.getDireccionDestinoId()));
        }

        return result;
    }
    @GetMapping("/tiendas/{id}")
    public String detalleTienda(@PathVariable(value="id") Long idUsu,Model model, HttpSession session){
        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null)
            return "redirect:/tiendas";

        UsuarioData tienda = usuarioService.findById(idUsu);
        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        model.addAttribute("tienda", tienda);
        model.addAttribute("envios",envioService.enviosTienda(idUsu,null));
        model.addAttribute("direcciones", direccionesDeEnvios(envioService.enviosTienda(idUsu,null)));
        return "detalleTienda";
    }

    @GetMapping("/tiendas/nuevo")
    public String nuevoTienda(Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        if(model.getAttribute("tienda") == null){
            model.addAttribute("tienda", new UsuarioData());
        }

        return "formNuevaTienda";
    }

    @PostMapping("/tiendas/nuevo")
    public String altaTienda(@ModelAttribute UsuarioData tienda, Model model, RedirectAttributes flash) {

        comprobarUsuarioLogeadoWebMaster();
        //tienda.getDireccion().setTelefono(tienda.getTelefono());
        //tienda.getDireccion().setNombre(tienda.getNombre());

        if (!tienda.getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}") || !tienda.getDireccion().getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}")){
            flash.addFlashAttribute("error", "Los números de telefono solo pueden contener el código de país precedido de un simbolo + (opcional) y nueve números. Sin espacios");
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/nuevo";
        }
        if(!tienda.getDireccion().getCodigoPostal().matches("[0-9]{5}")){
            flash.addFlashAttribute("error", "El código postal solo puede contener 5 dígitos");
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/nuevo";
        }

        try{
            usuarioService.registrarTienda(tienda,  new DireccionData(tienda.getDireccion()));
        } catch (Exception e){
            flash.addFlashAttribute("error", e.getMessage());
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/nuevo";
        }

        return "redirect:/tiendas";
    }

    @GetMapping("/tiendas/{id}/modificar")
    public String modificarRepartidor(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null){
            return "redirect:/tiendas";
        }

        UsuarioData tienda = usuarioService.findById(idUsu);
        if(model.getAttribute("tienda") == null){
            model.addAttribute("tienda", tienda);
        }

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));

        return "formModificarTienda";
    }

    @PostMapping("/tiendas/{id}/modificar")
    public String modRepartidor(@PathVariable(value="id") Long idUsu, @ModelAttribute UsuarioData tienda, Model model, RedirectAttributes flash) {

        comprobarUsuarioLogeadoWebMaster();

        if (usuarioService.findById(idUsu) == null || !idUsu.equals(tienda.getId())){
            return "redirect:/tiendas";
        }

        if (!tienda.getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}") || !tienda.getDireccion().getTelefono().matches("(\\+[0-9]{1,3})?[0-9]{9}")){
            flash.addFlashAttribute("error", "Los números de telefono solo pueden contener el código de país precedido de un simbolo + (opcional) y nueve números. Sin espacios");
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/" + idUsu + "/modificar";
        }
        if(!tienda.getDireccion().getCodigoPostal().matches("[0-9]{5}")){
            flash.addFlashAttribute("error", "El código postal solo puede contener 5 dígitos");
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/" + idUsu + "/modificar";
        }
        try{
            usuarioService.modificarTienda(tienda, idUsu);
        } catch (Exception e){
            flash.addFlashAttribute("error", e.getMessage());
            flash.addFlashAttribute("tienda", tienda);

            return "redirect:/tiendas/" + idUsu + "/modificar";
        }

        return "redirect:/tiendas";
    }

}
