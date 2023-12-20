package iwebpaqueteria.service;

import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.model.Direccion;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.model.Tarifa;
import iwebpaqueteria.repository.DireccionRepository;
import iwebpaqueteria.repository.EnvioRepository;
import iwebpaqueteria.repository.TarifaRepository;
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
    private ModelMapper modelMapper;

    @Transactional
    public EnvioData crearEnvio(float peso, int bultos, Long tarifaDistanciaId, Long tarifaBultosId, String observaciones, Long direccionOrigenId, Long direccionDestinoId) {
        logger.debug("Creación de envío  de dirección origen " + direccionOrigenId + " a dirección destino " + direccionDestinoId);
        Direccion direccionOrigen = direccionRepository.findById(direccionOrigenId).orElse(null);
        Direccion direccionDestino = direccionRepository.findById(direccionDestinoId).orElse(null);

        Tarifa tarifaDistancia = tarifaRepository.findById(tarifaDistanciaId).orElse(null);
        Tarifa tarifaBultos = tarifaRepository.findById(tarifaBultosId).orElse(null);

        if (direccionOrigen == null || direccionDestino == null || tarifaDistancia == null || tarifaBultos == null) {
            throw new IllegalArgumentException("No se ha podido crear el envío");
        }

        float precio = calcularCoste(direccionDestino.getCodigoPostal(), bultos);

        Envio envio = new Envio(peso, bultos, precio, observaciones, direccionOrigen, direccionDestino);
        envio.addTarifa(tarifaDistancia);
        envio.addTarifa(tarifaBultos);
        envioRepository.save(envio);
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

        return tarifaDistancia.getCoste() + tarifaBultos.getCoste() * bultos;
    }

}