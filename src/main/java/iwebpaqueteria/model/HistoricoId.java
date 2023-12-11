package iwebpaqueteria.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class HistoricoId implements Serializable {
    private Long envioId;
    private Long estadoId;
}
