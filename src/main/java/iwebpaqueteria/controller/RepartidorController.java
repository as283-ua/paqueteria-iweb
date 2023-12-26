package iwebpaqueteria.controller;

import antlr.StringUtils;
import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class RepartidorController {

    @Autowired
    ManagerUserSession managerUserSession;

    @Autowired
    UsuarioService usuarioService;

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

    @GetMapping("/repartidores/nuevo")
    public String nuevoRepartidor(Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
        model.addAttribute("repartidor", new UsuarioData());

        return "formNuevoRepartidor";
    }

    @PostMapping("/repartidores/nuevo")
    public String altaRepartidor(@ModelAttribute UsuarioData repartidor, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        if (!repartidor.getTelefono().matches("[0-9+]+")){
            model.addAttribute("error", "El teléfono puede contener solo números y el símbolo +");
            model.addAttribute("usuario", usuarioService.findById(managerUserSession.usuarioLogeado()));
            model.addAttribute("repartidor", new UsuarioData());

            return "formNuevoRepartidor";
        }

        usuarioService.registrarRepartidor(repartidor);

        return "redirect:/repartidores";
    }
}
