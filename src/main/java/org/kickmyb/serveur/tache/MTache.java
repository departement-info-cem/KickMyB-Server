package org.kickmyb.serveur.tache;

import jakarta.persistence.*;
import org.kickmyb.serveur.photo.MPhoto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MTache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public Date dateCreation;
    public Date dateLimite;


    @Convert(converter = Encrypteur.class)  // TODO exemple stupide, servirait plutôt pour NAS ou numero carte crédit
    public String name;

    @OneToMany(fetch=FetchType.EAGER)
    public List<MProgressEvent> events = new ArrayList<>();

    @OneToOne
    public MPhoto photo;

}
