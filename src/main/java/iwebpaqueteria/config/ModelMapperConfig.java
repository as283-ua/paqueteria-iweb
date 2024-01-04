package iwebpaqueteria.config;

import iwebpaqueteria.dto.EnvioData;
import iwebpaqueteria.dto.HistoricoData;
import iwebpaqueteria.model.Envio;
import iwebpaqueteria.model.Historico;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new AbstractConverter<Historico, HistoricoData>() {
            @Override
            protected HistoricoData convert(Historico historico) {
                HistoricoData historicoData = new HistoricoData();
                historicoData.setFecha(historico.getFecha().toString());
                historicoData.setObservaciones(historico.getObservaciones());
                historicoData.setEstado(historico.getEstado().getNombre());
                return historicoData;
            }
        });

        modelMapper.addConverter(new AbstractConverter<Envio, EnvioData>() {
            @Override
            protected EnvioData convert(Envio envio) {
                EnvioData envioData = new EnvioData();
                envioData.setCodigo(envio.getCodigo());
                envioData.setBultos(envio.getBultos());
                envioData.setDireccionDestinoId(envio.getDireccionDestino().getId());
                envioData.setDireccionOrigenId(envio.getDireccionOrigen().getId());
                envioData.setHistoricoIds(envio.getHistoricos().stream().
                        map(Historico::getId).
                        collect(java.util.stream.Collectors.toList()));
                envioData.setId(envio.getId());
                envioData.setObservaciones(envio.getObservaciones());
                envioData.setPeso(envio.getPeso());
                envioData.setPrecio(envio.getPrecio());

                envioData.setRepartidorId(envio.getRepartidor() == null ? null : envio.getRepartidor().getId());
                envioData.setTarifasIds(envio.getTarifas().stream().map(tarifa -> tarifa.getId()).collect(java.util.stream.Collectors.toList()));
                return envioData;
            }
        });

        return modelMapper;
    }
}
