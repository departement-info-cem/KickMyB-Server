package org.kickmyb.server.model;

import org.kickmyb.server.AttributeEncryptor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MTask {

    @Id     @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    // But the key is still stored on server no? Yes, only way would be to push it on another machine
    // it could be a crypto service or another machine in our infrastructure
    @Convert(converter = AttributeEncryptor.class)  // TODO exemple stupide, servirait plutôt pour NAS ou numero carte crédit
    public String name;

    @OneToMany(fetch=FetchType.EAGER)
    public List<MEvent> events = new ArrayList<>();

}
