package iwebpaqueteria.config;

import iwebpaqueteria.dto.HistoricoData;
import iwebpaqueteria.model.Historico;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

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

        return modelMapper;
    }
}
