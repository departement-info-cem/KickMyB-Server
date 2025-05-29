package org.kickmyb.serveur.utilisateur;

import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
public class ServiceUtilisateur implements UserDetailsService {

    public static class MauvaisNomOuMotDePasse extends Exception {}
    public static class NomTropCourt extends Exception {}
    public static class NomDejaPris extends Exception {}
    public static class MotDePasseTropCourt extends Exception {}
    public static class MotsDePasseDifferents extends Exception {}

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DepotUtilisateur userRepository;

    // This one has to be there by convention to fit with Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUtilisateur user = userRepository.findByNom(username.trim().toLowerCase()).get();
        User u = new User(user.nom, user.motDePasse, new ArrayList<>());
        return u;
    }


    // https://stackoverflow.com/questions/36498327/catch-dataintegrityviolationexception-in-transactional-service-method
    @Transactional(rollbackFor = NomDejaPris.class)
    public void inscrire(RequeteInscription req)
            throws NomTropCourt, MotDePasseTropCourt, NomDejaPris, MotsDePasseDifferents {
        String username = req.nom.toLowerCase().trim();
        if (!req.motDePasse.equals(req.confirmationMotDePasse)) throw new MotsDePasseDifferents();
        if (username.length() < 2) throw new NomTropCourt();
        if (req.motDePasse.length() < 4) throw new MotDePasseTropCourt();
        // validation de l'unicité est faite au niveau de la BD voir MUser.java
        try {
            MUtilisateur p = new MUtilisateur();
            p.nom = username;
            p.motDePasse = passwordEncoder.encode(req.motDePasse);
            userRepository.saveAndFlush(p);
        } catch (DataIntegrityViolationException e) {
            throw new NomDejaPris();
        }
    }
}
