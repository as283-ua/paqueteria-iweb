package iwebpaqueteria.service;

import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.EnvioReducidoData;
import iwebpaqueteria.dto.RangoFechas;
import iwebpaqueteria.dto.TarifaData;
import iwebpaqueteria.model.*;
import iwebpaqueteria.repository.*;
import iwebpaqueteria.service.exception.EnvioServiceException;
import iwebpaqueteria.service.exception.UsuarioServiceException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Target;
import java.util.*;
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
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private HistoricoRepository historicoRepository;


    @Transactional(readOnly = true)
    public List<EnvioData> findAll() {
        logger.debug("Buscando todos los envíos");
        List<Envio> envios = (List<Envio>) envioRepository.findAll();
        return envios.stream()
                .map(envio -> modelMapper.map(envio, EnvioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnvioData> findAll(RangoFechas rangoFechas) {
        if(rangoFechas == null) {
            return findAll();
        }

        logger.debug("Buscando todos los envíosc desde " + rangoFechas.getFechaInicio() + " hasta " + rangoFechas.getFechaFin());

        Set<Envio> envios = new HashSet<>((List<Envio>)envioRepository.findAll());
        boolean todosEnvios = rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() == null;

        if (todosEnvios) {
            return envios.stream().map(envio -> modelMapper.map(envio, EnvioData.class)).collect(Collectors.toList());
        }

        return filtrarEnvioFecha(envios, rangoFechas);
    }

    @Transactional(readOnly = true)
    public List<EnvioData> getEnviosRepartidor(Long idUsu) {
        logger.debug("Buscando todos los envíos del repartidor con id " + idUsu);
        Usuario repartidor = usuarioRepository.findById(idUsu).orElse(null);
        Set<Envio> envios = repartidor.getEnvios();
        return envios.stream()
                .map(envio -> modelMapper.map(envio, EnvioData.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnvioData recuperarEnvio(Long id) {
        logger.debug("Buscando envío con id " + id);
        Envio envio =  envioRepository.findById(id).orElse(null);
        if(envio == null)
            return null;

        return modelMapper.map(envio, EnvioData.class);
    }

    @Transactional
    public EnvioData crearEnvio(float peso, int bultos, String observaciones, Long tiendaId, Long direccionDestinoId) {
        logger.debug("Creando envío");
        Usuario tienda = usuarioRepository.findById(tiendaId).orElseThrow(() -> new EnvioServiceException("Tienda con ID " + tiendaId + " no existe"));
        Direccion direccionDestino = direccionRepository.findById(direccionDestinoId).orElseThrow(() -> new EnvioServiceException("Direccion con ID " + direccionDestinoId + " no existe"));
        Direccion direccionOrigen = tienda.getDireccion();

        if(direccionOrigen == null)
            throw new EnvioServiceException("La tienda no tiene dirección");

        float precio = this.calcularCoste(direccionDestino.getCodigoPostal(), bultos);

        Envio envio = new Envio(peso, bultos, precio, observaciones, direccionOrigen, direccionDestino);
        envio = envioRepository.save(envio);

        Estado estado = estadoRepository.findByNombre("En almacén").
                orElseThrow(() -> new EnvioServiceException("Error interno: no existe estado En almacén"));

        Historico envioEnAlmacenH = new Historico(envio, estado);

        envioEnAlmacenH = historicoRepository.save(envioEnAlmacenH);
        EnvioData envioDTO = modelMapper.map(envio, EnvioData.class);

        return envioDTO;
    }

    @Transactional(readOnly = true)
    public Tarifa calcularTarifaDistancia(String codigoPostal){
        logger.debug("Cálculo de tarifa de distancia");

        // Comprobamos que el código postal empieza con 03
        Tarifa tarifa = null;
        if (codigoPostal.startsWith("03")) {
            tarifa = tarifaRepository.findByNombre("Corta distancia")
                    .orElseThrow(() -> new EnvioServiceException("No existe la tarifa corta distancia"));
        }else {
            tarifa = tarifaRepository.findByNombre("Larga distancia")
                    .orElseThrow(() -> new EnvioServiceException("No existe la tarifa larga distancia"));
        }
        return tarifa;
    }

    @Transactional(readOnly = true)
    public Float calcularCoste(String codigoPostal, int bultos){
        logger.debug("Cálculo de coste de envío");
        Tarifa tarifaDistancia = calcularTarifaDistancia(codigoPostal);
        Tarifa tarifaBultos = tarifaRepository.findByNombre("Bultos")
                .orElseThrow(() -> new EnvioServiceException("No existe la tarifa bultos"));

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

    @Transactional
    public void asignarRepartidor(Long idEnvio, String nombreRepartidor){
        logger.debug("Asignando repartidor");
        Envio envio = envioRepository.findById(idEnvio).orElse(null);
        if(envio == null)
            throw new IllegalArgumentException("No existe envío con id " + idEnvio);

        Usuario repartidor = usuarioRepository.findByNombre(nombreRepartidor).orElse(null);

        if(repartidor == null)
            throw new IllegalArgumentException("No existe un repartidor con ese nombre");

        envio.setRepartidor(repartidor);
        envioRepository.save(envio);
    }

    @Transactional
    public void cancelarEnvio(String codigoEnvio, String observaciones) {
        logger.debug("Cancelando envío");
        Envio envio = envioRepository.findByCodigo(codigoEnvio).
                orElseThrow(() -> new EnvioServiceException("No existe envío con código " + codigoEnvio));

        Estado estado = estadoRepository.findByNombre("Cancelado").
                orElseThrow(() -> new EnvioServiceException("Error interno: no existe estado Cancelado"));

        Historico envioCanceladoH = new Historico(envio, estado);
        envioCanceladoH.setObservaciones(observaciones);

        envioCanceladoH = historicoRepository.save(envioCanceladoH);
    }

    @Transactional
    public EnvioReducidoData resumenEnvio(String codigoEnvio) {
        Envio envio = envioRepository.findByCodigo(codigoEnvio).
                orElseThrow(() -> new EnvioServiceException("No existe envío con código " + codigoEnvio));

        EnvioReducidoData envioResult = modelMapper.map(envio, EnvioReducidoData.class);

        return envioResult;
    }

    private List<EnvioReducidoData> filtrarEnviosFechaReducido(Set<Envio> envios, RangoFechas rangoFechas) {
        List<EnvioReducidoData> enviosReducidos = new ArrayList<>();
        Estado enAlmacen = estadoRepository.findByNombre("En almacén").orElse(null);

        if (enAlmacen == null)
            throw new UsuarioServiceException("No existe el estado En almacén");

        for (Envio envio : envios) {
            for(Historico historico : envio.getHistoricos()) {
                if(!historico.getEstado().equals(enAlmacen)){
                    continue;
                }

                boolean soloInicio = rangoFechas.getFechaInicio() != null && rangoFechas.getFechaFin() == null
                        && historico.getFecha().toLocalDate().isAfter(rangoFechas.getFechaInicio());
                if(soloInicio) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioReducidoData.class));
                    break;
                }

                boolean soloFin = rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() != null
                        && historico.getFecha().toLocalDate().isBefore(rangoFechas.getFechaFin());
                if(soloFin) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioReducidoData.class));
                    break;
                }

                boolean enRango = historico.getFecha().toLocalDate().isAfter(rangoFechas.getFechaInicio()) && historico.getFecha().toLocalDate().isBefore(rangoFechas.getFechaFin());
                if(enRango) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioReducidoData.class));
                    break;
                }
            }
        }
        return enviosReducidos;
    }

    private List<EnvioData> filtrarEnvioFecha(Set<Envio> envios, RangoFechas rangoFechas) {
        List<EnvioData> enviosReducidos = new ArrayList<>();
        Estado enAlmacen = estadoRepository.findByNombre("En almacén").orElse(null);

        if (enAlmacen == null)
            throw new UsuarioServiceException("No existe el estado En almacén");

        for (Envio envio : envios) {
            for(Historico historico : envio.getHistoricos()) {
                if(!historico.getEstado().equals(enAlmacen)){
                    continue;
                }

                boolean soloInicio = rangoFechas.getFechaInicio() != null && rangoFechas.getFechaFin() == null
                        && historico.getFecha().toLocalDate().isAfter(rangoFechas.getFechaInicio());
                if(soloInicio) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioData.class));
                    break;
                }

                boolean soloFin = rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() != null
                        && historico.getFecha().toLocalDate().isBefore(rangoFechas.getFechaFin());
                if(soloFin) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioData.class));
                    break;
                }

                boolean enRango = historico.getFecha().toLocalDate().isAfter(rangoFechas.getFechaInicio()) && historico.getFecha().toLocalDate().isBefore(rangoFechas.getFechaFin());
                if(enRango) {
                    enviosReducidos.add(modelMapper.map(envio, EnvioData.class));
                    break;
                }
            }
        }
        return enviosReducidos;
    }

    @Transactional
    public List<EnvioReducidoData> enviosReducidosTienda(Long idTienda, RangoFechas rangoFechas) {
        List<EnvioReducidoData> envios;

        Usuario tienda = usuarioRepository.findById(idTienda).orElse(null);
        if(tienda == null)
            throw new UsuarioServiceException("Tienda con ID " + idTienda + " no existe");

        Set<Envio> envioEntities = tienda.getDireccion().getEnviosOrigen();

        boolean todosEnvios = rangoFechas == null || rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() == null;

        if(todosEnvios){
            envios = envioEntities.stream().map(envio -> modelMapper.map(envio, EnvioReducidoData.class)).collect(Collectors.toList());
            return envios;
        }

        envios = filtrarEnviosFechaReducido(envioEntities, rangoFechas);
        return envios;
    }

    @Transactional
    public List<EnvioData> enviosRepartidor(Long repartidorId, RangoFechas rangoFechas) {
        Usuario repartidor = usuarioRepository.findById(repartidorId).orElse(null);
        if (repartidor == null)
            throw new UsuarioServiceException("Repartidor con ID " + repartidorId + " no existe");

        Set<Envio> envioEntities = repartidor.getEnvios();

        boolean todosEnvios = rangoFechas == null || rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() == null;

        if (todosEnvios) {
            return envioEntities.stream().map(envio -> modelMapper.map(envio, EnvioData.class)).collect(Collectors.toList());
        }

        return filtrarEnvioFecha(envioEntities, rangoFechas);
    }

    @Transactional
    public List<EnvioData> enviosTienda(Long tiendaId, RangoFechas rangoFechas) {
        Usuario tienda = usuarioRepository.findById(tiendaId).orElse(null);
        if (tienda == null)
            throw new UsuarioServiceException("Tienda con ID " + tiendaId + " no existe");

        Set<Envio> envioEntities = tienda.getDireccion().getEnviosOrigen();

        boolean todosEnvios = rangoFechas == null || rangoFechas.getFechaInicio() == null && rangoFechas.getFechaFin() == null;

        if (todosEnvios) {
            return envioEntities.stream().map(envio -> modelMapper.map(envio, EnvioData.class)).collect(Collectors.toList());
        }

        return filtrarEnvioFecha(envioEntities, rangoFechas);
    }
}