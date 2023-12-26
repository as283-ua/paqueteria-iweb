package iwebpaqueteria.service;

import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.TarifaData;
import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.model.Tarifa;
import iwebpaqueteria.model.Usuario;
import iwebpaqueteria.repository.DireccionRepository;
import iwebpaqueteria.repository.EnvioRepository;
import iwebpaqueteria.repository.TarifaRepository;
import iwebpaqueteria.repository.UsuarioRepository;
import iwebpaqueteria.service.exception.EnvioServiceException;
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
public class EnvioService {

    Logger logger = LoggerFactory.getLogger(EnvioService.class);

    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private DireccionRepository direccionRepository;
    @Autowired
    private TarifaRepository tarifaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Transactional(readOnly = true)
    public List<EnvioData> findAll() {
        logger.debug("Buscando todos los envíos");
        List<Envio> envios = (List<Envio>) envioRepository.findAll();
        return envios.stream()
                .map(envio -> modelMapper.map(envio, EnvioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnvioData recuperarEnvio(Long id) {
        logger.debug("Buscando envío con id " + id);
        Envio envio =  envioRepository.findById(id).orElse(null);
        if(envio == null)
            throw new IllegalArgumentException("No existe envío con id " + id);

        return modelMapper.map(envio, EnvioData.class);
    }

    @Transactional
    public EnvioData crearEnvio(float peso, int bultos, String observaciones, Long tiendaId, Long direccionDestinoId) {
        Usuario tienda = usuarioRepository.findById(tiendaId).orElseThrow(() -> new EnvioServiceException("Tienda con ID " + tiendaId + " no existe"));
        Direccion direccionDestino = direccionRepository.findById(direccionDestinoId).orElseThrow(() -> new EnvioServiceException("Direccion con ID " + direccionDestinoId + " no existe"));
        Direccion direccionOrigen = tienda.getDireccion();

        if(direccionOrigen == null)
            throw new EnvioServiceException("La tienda no tiene dirección");

        float precio = this.calcularCoste(direccionDestino.getCodigoPostal(), bultos);

        Envio envio = new Envio(peso, bultos, precio, observaciones, direccionOrigen, direccionDestino);
        envio = envioRepository.save(envio);

        return modelMapper.map(envio, EnvioData.class);
    }

    @Transactional(readOnly = true)
    public Tarifa calcularTarifaDistancia(String codigoPostal){
        logger.debug("Cálculo de tarifa de distancia");

        // Comprobamos que el código postal empieza con 03
        Tarifa tarifa = null;
        if (codigoPostal.startsWith("03")) {
            tarifa = tarifaRepository.findByNombre("Corta distancia");
        }else {
            tarifa = tarifaRepository.findByNombre("Larga distancia");
        }
        return tarifa;
    }

    @Transactional(readOnly = true)
    public Float calcularCoste(String codigoPostal, int bultos){
        logger.debug("Cálculo de coste de envío");
        Tarifa tarifaDistancia = calcularTarifaDistancia(codigoPostal);
        Tarifa tarifaBultos = tarifaRepository.findByNombre("Bultos");

        float coste = 0;
        coste += tarifaDistancia != null ? tarifaDistancia.getCoste() : 0;
        coste += tarifaBultos != null ? tarifaBultos.getCoste() : 0;

        return coste;
    }

    @Transactional(readOnly = true)
    public Float calcularPrecioTotal(List<EnvioData> envios){
        logger.debug("Cálculo de precio total de envíos");
        Float precioTotal = 0f;
        for (EnvioData envio : envios) {
            precioTotal += envio.getPrecio();
        }
        return precioTotal;
    }

    @Transactional(readOnly = true)
    public String tarifasDeEnvio(Long id) {
        logger.debug("Buscando tarifas de envío con id " + id);
        Envio envio = envioRepository.findById(id).orElse(null);
        if(envio == null)
            throw new IllegalArgumentException("No existe envío con id " + id);

        return envio.getTarifas().stream()
                .map(tarifa -> modelMapper.map(tarifa, TarifaData.class))
                .map(TarifaData::getNombre)
                .collect(Collectors.joining(", "));
    }

}