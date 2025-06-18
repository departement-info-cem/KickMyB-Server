package org.kickmyb.serveur.photo;

import jakarta.persistence.*;
import org.kickmyb.serveur.tache.MTache;


@Entity
public class MPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob
    public byte[] blob;
    @Basic  public String typeContenu;
    @OneToOne(mappedBy = "photo")
    public MTache tache;
}
