package iwebpaqueteria.service;

import iwebpaqueteria.dto.UsuarioData;
import iwebpaqueteria.model.Rol;
import iwebpaqueteria.repository.RolRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolService {
    @Autowired
    RolRepository rolRepository;
    @Autowired
    ModelMapper modelMapper;
    @Transactional(readOnly = true)
    public Set<UsuarioData> listarUsuariosPorRol(String nombreRol){
        Optional<Rol> r = rolRepository.findByNombre(nombreRol);

        if(r.isPresent()){
            return r.get().getUsuarios().stream().map(tienda -> modelMapper.map(tienda, UsuarioData.class)).collect(Collectors.toSet());
        }

        return new HashSet<>();
    }
}
