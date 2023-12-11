package iwebpaqueteria.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "estados")
public class Estado {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String nombre;

    @OneToMany(mappedBy = "estado")
    private Set<Historico> historicos;
}
