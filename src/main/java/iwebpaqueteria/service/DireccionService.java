package iwebpaqueteria.service;

import iwebpaqueteria.dto.DireccionData;
import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.repository.DireccionRepository;
import iwebpaqueteria.repository.EnvioRepository;
import iwebpaqueteria.repository.TarifaRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DireccionService {
    Logger logger = LoggerFactory.getLogger(EnvioService.class);
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;

    public DireccionData crearDireccion(DireccionData direccionData) {
        logger.debug("Creando dirección");
        Direccion direccion = modelMapper.map(direccionData, Direccion.class);
        direccion = direccionRepository.save(direccion);
        return modelMapper.map(direccion, DireccionData.class);
    }

    public DireccionData findById(Long id) {
        logger.debug("Buscando dirección por id");
        Direccion direccion = direccionRepository.findById(id).orElse(null);
        return modelMapper.map(direccion, DireccionData.class);
    }
}
