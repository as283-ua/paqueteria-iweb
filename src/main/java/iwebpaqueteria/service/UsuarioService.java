package iwebpaqueteria.service;

import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.model.Usuario;
import iwebpaqueteria.repository.RolRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import iwebpaqueteria.service.exception.UsuarioServiceException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    public enum LoginStatus {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD};

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public LoginStatus login(String eMail, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(eMail);
        if (!usuario.isPresent()) {
            return LoginStatus.USER_NOT_FOUND;
        } else if (!usuario.get().getContrasenya().equals(password)) {
            return LoginStatus.ERROR_PASSWORD;
        } else {
            return LoginStatus.LOGIN_OK;
        }
    }

    @Transactional
    public UsuarioData registrarRepartidor(UsuarioData usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getContrasenya() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else {
            Usuario usuarioNuevo = modelMapper.map(usuario, Usuario.class);
            usuarioNuevo.setRol(rolRepository.findByNombre("repartidor"));
            usuarioNuevo = usuarioRepository.save(usuarioNuevo);
            return modelMapper.map(usuarioNuevo, UsuarioData.class);
        }
    }

    @Transactional
    public UsuarioData registrarTienda(UsuarioData usuario) {
        Optional<Usuario> usuarioBD = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioBD.isPresent())
            throw new UsuarioServiceException("El usuario " + usuario.getEmail() + " ya está registrado");
        else if (usuario.getEmail() == null)
            throw new UsuarioServiceException("El usuario no tiene email");
        else if (usuario.getContrasenya() == null)
            throw new UsuarioServiceException("El usuario no tiene password");
        else {
            Usuario usuarioNuevo = modelMapper.map(usuario, Usuario.class);
            usuarioNuevo.setRol(rolRepository.findByNombre("tienda"));
            usuarioNuevo = usuarioRepository.save(usuarioNuevo);
            return modelMapper.map(usuarioNuevo, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioData findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioData findById(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return null;
        else {
            return modelMapper.map(usuario, UsuarioData.class);
        }
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> getUsuariosRegistrados() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> modelMapper.map(usuario, UsuarioData.class)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> getRepartidoresRegistrados() {
        List<Usuario> usuarios = (List<Usuario>) usuarioRepository.findAll();
        List<Usuario> repartidores = usuarios.stream().filter(usuario -> "repartidor".equals(usuario.getRol().getNombre())).collect(Collectors.toList());
        return repartidores.stream().map(repartidor -> modelMapper.map(repartidor, UsuarioData.class)).collect(Collectors.toList());
    }

    @Transactional
    public void borrarUsuario(Long id) {
        Optional<Usuario> usuarioBD = usuarioRepository.findById(id);
        if (usuarioBD.isPresent()){
            usuarioRepository.delete(usuarioBD.get());
        }
    }
}
