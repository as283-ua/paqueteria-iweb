package iwebpaqueteria.controller;
import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.repository.RolRepository;
import iwebpaqueteria.service.RolService;
import iwebpaqueteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class TiendaController {
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ManagerUserSession managerUserSession;
    @Autowired
    RolService rolService;
    @GetMapping("/tiendas/{userId}/APIKEY")
    public String mostrarApiKey(@PathVariable(value = "userId") Long idTienda, Model model, HttpSession session) {
        comprobarUsuarioLogeado();

        Long idYo = managerUserSession.usuarioLogeado();
        if (idYo!=null && !idYo.equals(idTienda)) {
            throw new UsuarioSinPermisosException();
        }
        model.addAttribute("usuario", usuarioService.findById(idYo));
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

        return "listadoTiendas";
    }

    @DeleteMapping("/tiendas/{id}")
    public String borrarTienda(@PathVariable(value="id") Long idUsu, Model model, HttpSession session) {

        comprobarUsuarioLogeadoWebMaster();

        usuarioService.borrarUsuario(idUsu);

        return "redirect:/tiendas";
    }

}
