package iwebpaqueteria.controller;

import iwebpaqueteria.authentication.ManagerUserSession;
import iwebpaqueteria.controller.exception.EnvioNotFoundException;
import iwebpaqueteria.controller.exception.UsuarioNoLogeadoException;
import iwebpaqueteria.dto.*;
import iwebpaqueteria.controller.exception.UsuarioSinPermisosException;
import iwebpaqueteria.model.Historico;
import iwebpaqueteria.service.DireccionService;
import iwebpaqueteria.service.UsuarioService;
import iwebpaqueteria.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
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

    private void comprobarUsuarioLogeadoRepartidor() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
        if(!"repartidor".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre())){
            throw new UsuarioSinPermisosException();
        }
    }

    private void compruebaUsuarioLogeadoTienda() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
        if(!"tienda".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre())){
            throw new UsuarioSinPermisosException();
        }
    }

    private void compruebaUsuarioLogeadoTiendaOWebMaster() {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado==null)
            throw new UsuarioNoLogeadoException();
        if(!"tienda".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre()) && !"webmaster".equals(usuarioService.findById(idUsuarioLogeado).getRol().getNombre())){
            throw new UsuarioSinPermisosException();
        }
    }

    @GetMapping("/")
    public String buscarEnvio(Model model) {
        Long idYo = managerUserSession.usuarioLogeado();
        if (idYo!=null)
            model.addAttribute("usuario", usuarioService.findById(idYo));

        return "buscarEnvio";
    }

    public Map<Long, DireccionData> direccionesDeEnvios(List<EnvioData> envios){
        Map<Long, DireccionData> result = new HashMap<>();

        for (EnvioData envio : envios) {
            result.put(envio.getId(), direccionService.findById(envio.getDireccionDestinoId()));
        }

        return result;
    }

    @GetMapping("/envios")
    public String listadoEnvios(Model model, @ModelAttribute FiltroEnvios filtroEnvios) {

        comprobarUsuarioLogeado();

        UsuarioData usuario = usuarioService.findById(managerUserSession.usuarioLogeado());

        FiltroEnvios filtro = null;
        if (filtroEnvios != null){
            if(filtroEnvios.isHoy()){
                filtro = new FiltroEnvios(filtroEnvios);
                filtroEnvios.setFechaInicio(LocalDate.now());
                filtroEnvios.setFechaFin(LocalDate.now());
            } else {
                filtro = new FiltroEnvios(filtroEnvios);
                if(filtroEnvios.getFechaInicio() != null){
                    filtro.setFechaInicio(filtroEnvios.getFechaInicio().minusDays(1));
                }
                if(filtroEnvios.getFechaFin() != null){
                    filtro.setFechaFin(filtroEnvios.getFechaFin().plusDays(1));
                }
            }
        }

        List<EnvioData> envios;
        if(usuario.getRol().getNombre().equalsIgnoreCase("webmaster"))
            envios = envioService.findAll(filtro);
        else if(usuario.getRol().getNombre().equalsIgnoreCase("tienda"))
            envios = envioService.enviosTienda(usuario.getId(), filtro);
        else if(usuario.getRol().getNombre().equalsIgnoreCase("repartidor"))
            envios = envioService.enviosRepartidor(usuario.getId(), filtro);
        else
            throw new UsuarioSinPermisosException();

        Map<Long, DireccionData> direcciones = direccionesDeEnvios(envios);
        Float precioTotal = envioService.calcularPrecioTotal(envios);

        model.addAttribute("rangoFechas", filtroEnvios);
        model.addAttribute("usuario", usuario);
        model.addAttribute("envios", envios);
        model.addAttribute("direcciones", direcciones);
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

        Historico estadoActual = envioService.getEstadoActual(idEnvio);

        if(estadoActual.getEstado().getId() == 1 || estadoActual.getEstado().getId() == 2 || estadoActual.getEstado().getId() == 3 || estadoActual.getEstado().getId() == 4)
            model.addAttribute("avanzarEstado", new AvanzarEstadoData());

        model.addAttribute("usuario", usuario);
        model.addAttribute("envio", envio);
        model.addAttribute("direccionOrigen", direccionService.findById(envio.getDireccionOrigenId()));
        model.addAttribute("direccionDestino", direccionService.findById(envio.getDireccionDestinoId()));
        model.addAttribute("tarifas", envioService.tarifasDeEnvio(idEnvio));
        model.addAttribute("estadoActual", estadoActual);
        model.addAttribute("historico", envioService.historicoDeEnvio(idEnvio));

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

    @GetMapping("/envios/{id}/desasignar")
    public String desasignarEnvio(Model model, @PathVariable(value="id") Long idEnvio) {

        comprobarUsuarioLogeadoWebMaster();

        envioService.desasignarRepartidor(idEnvio);

        return "redirect:/envios/" + idEnvio;
    }

    @PostMapping("/envios/{id}/avanzarEstado")
    public String avanzarEstadoEnvio(@PathVariable(value="id") Long idEnvio, @ModelAttribute AvanzarEstadoData avanzarEstadoData, Model model, HttpSession session){

        comprobarUsuarioLogeadoRepartidor();
        Long repEnvio = envioService.recuperarEnvio(idEnvio).getRepartidorId();
        Long logeado = managerUserSession.usuarioLogeado();
        if(!envioService.recuperarEnvio(idEnvio).getRepartidorId().equals(managerUserSession.usuarioLogeado()))
            throw new UsuarioSinPermisosException();

        envioService.avanzarEstado(idEnvio, avanzarEstadoData.getObservaciones());
        return "redirect:/envios/" + idEnvio;
    }

    @PutMapping("/envios/{id}/ausente")
    public String estadoEnvioAusente(@PathVariable(value="id") Long idEnvio, Model model, HttpSession session){

        comprobarUsuarioLogeadoRepartidor();
        if(!envioService.recuperarEnvio(idEnvio).getRepartidorId().equals(managerUserSession.usuarioLogeado()))
            throw new UsuarioSinPermisosException();

        envioService.estadoAusente(idEnvio);
        return "redirect:/envios/" + idEnvio;
    }

    @PutMapping("/envios/{id}/rechazado")
    public String estadoEnvioRechazado(@PathVariable(value="id") Long idEnvio, Model model, HttpSession session){

        comprobarUsuarioLogeadoRepartidor();
        if(!envioService.recuperarEnvio(idEnvio).getRepartidorId().equals(managerUserSession.usuarioLogeado()))
            throw new UsuarioSinPermisosException();

        envioService.rechazarEnvio(idEnvio);
        return "redirect:/envios/" + idEnvio;
    }

    @PutMapping("/envios/{id}/deshacer")
    public String estadoEnvioDeshacer(@PathVariable(value="id") Long idEnvio, Model model, HttpSession session){

        comprobarUsuarioLogeadoRepartidor();
        if(!envioService.recuperarEnvio(idEnvio).getRepartidorId().equals(managerUserSession.usuarioLogeado()))
            throw new UsuarioSinPermisosException();

        envioService.deshacerEstadoEnvio(idEnvio);
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

    @GetMapping("/envios/{id}/cancelar")
    public String cancelarEnvio(Model model, @PathVariable(value="id") Long idEnvio) {

        compruebaUsuarioLogeadoTiendaOWebMaster();

        envioService.cancelarEnvio(idEnvio);

        return "redirect:/envios/" + idEnvio;
    }

}
