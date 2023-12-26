package iwebpaqueteria.controller;

import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.dto.LoginData;
import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ManagerUserSession managerUserSession;


    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "formLogin";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, HttpSession session) {

        // Llamada al servicio para comprobar si el login es correcto
        UsuarioService.LoginStatus loginStatus = usuarioService.login(loginData.geteMail(), loginData.getPassword());

        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.geteMail());

            managerUserSession.logearUsuario(usuario.getId());

            return "redirect:/envios";
        } else if (loginStatus == UsuarioService.LoginStatus.USER_NOT_FOUND) {
            model.addAttribute("error", "No existe usuario");
            return "formLogin";
        } else if (loginStatus == UsuarioService.LoginStatus.ERROR_PASSWORD) {
            model.addAttribute("error", "Contraseña incorrecta");
            return "formLogin";
        }
        return "formLogin";
    }

   @GetMapping("/logout")
   public String logout(HttpSession session) {
        managerUserSession.logout();
        return "redirect:/login";
   }
}
