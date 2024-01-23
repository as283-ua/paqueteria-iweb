package iwebpaqueteria.controller;
import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TiendaController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;
    @GetMapping("/tiendas/{userId}/APIKEY")
    public String mostrarApiKey(@PathVariable(value = "userId") Long idTienda, Model model, HttpSession session) {
        comprobarUsuarioLogeado();

        Long idYo = managerUserSession.usuarioLogeado();
        if (idYo!=null && idYo.equals(idTienda)) {
            usuarioService.cambiarApiKey(idTienda);
        }

        model.addAttribute("tienda", usuarioService.findById(idTienda));
        return "APIKEY";
    }

    @PostMapping("/tiendas/{userId}/APIKEY")
    public String cambiarApiKey(@PathVariable(value = "userId") Long idTienda, Model model, HttpSession session){
        comprobarUsuarioLogeado();

        Long idYo = managerUserSession.usuarioLogeado();
        if (idYo!=null && idYo.equals(idTienda)) {
            usuarioService.cambiarApiKey(idTienda);
        }

        model.addAttribute("tienda", usuarioService.findById(idTienda));
        return "APIKEY";
    }

    private void comprobarUsuarioLogeado() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
    }

}
