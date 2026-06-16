package org.kickmyb.serveur.utilisateur;

import jakarta.persistence.*;
import org.kickmyb.serveur.tache.MTache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 15-09-15.
 */

@Entity
public class MUtilisateur {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public Long id;

    @Column(unique = true)
    public String nom;

    @Basic
    public String motDePasse;

    @Basic
    public String firebaseToken;

    @OneToMany(fetch=FetchType.EAGER)
    public List<MTache> taches = new ArrayList<>();
}
