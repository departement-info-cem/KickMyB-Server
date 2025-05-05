package org.kickmyb.serveur.utilisateur;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

// extends UserDetailsService which is one of the Spring Security entry points
public interface ServiceUtilisateur extends UserDetailsService {

    class MauvaisNomOuMotDePasse extends Exception {}

    class NomTropCourt extends Exception {}
    class NomDejaPris extends Exception {}
    class MotDePasseTropCourt extends Exception {}

    void inscrire(SignupRequest req) throws NomTropCourt, MotDePasseTropCourt, NomDejaPris;

}
