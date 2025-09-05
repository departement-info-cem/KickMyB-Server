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

    public static class MauvaisNomOuMotDePasse extends RuntimeException {}
    public static class NomTropCourt extends RuntimeException {}
    public static class NomTropLong extends RuntimeException {}
    public static class NomDejaPris extends RuntimeException {}
    public static class MotDePasseTropCourt extends RuntimeException {}
    public static class MotDePasseTropLong extends RuntimeException {}
    public static class MotsDePasseDifferents extends RuntimeException {}

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DepotUtilisateur userRepository;

    // Cette fonction vient de UserDetailsService donc en anglais
    @Override
    public UserDetails loadUserByUsername(String username) {
        MUtilisateur user = userRepository.findByNom(username.trim().toLowerCase()).get();
        UserDetails u = new User(user.nom, user.motDePasse, new ArrayList<>());
        return u;
    }

    // TODO si un nom ou mot de passe est très long, on lance la mauvaise exception
    // https://stackoverflow.com/questions/36498327/catch-dataintegrityviolationexception-in-transactional-service-method
    @Transactional(rollbackFor = NomDejaPris.class)
    public void inscrire(RequeteInscription req) {
        String username = req.nom.toLowerCase().trim();
        if (!req.motDePasse.equals(req.confirmationMotDePasse)) throw new MotsDePasseDifferents();
        if (username.length() < 2) throw new NomTropCourt();
        if (username.length() > 255) throw new NomTropLong();
        if (req.motDePasse.length() < 4) throw new MotDePasseTropCourt();
        if (req.motDePasse.length() > 255) throw new MotDePasseTropLong();
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

    @Transactional
    public void enregistrerTokenFirebase(String nomUtilisateur, String token) {
        MUtilisateur utilisateur = userRepository.findByNom(nomUtilisateur.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        utilisateur.firebaseToken = token;
        userRepository.save(utilisateur);
    }
}
