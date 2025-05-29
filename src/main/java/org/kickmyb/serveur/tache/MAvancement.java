package org.kickmyb.serveur.tache;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

/**
 * Created by joris on 15-09-15.
 */
@Entity
public class MAvancement {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long id;
    public int nouveauPourcentage;
    public Date date;

}
