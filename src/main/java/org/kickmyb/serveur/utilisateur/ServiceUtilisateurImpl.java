package org.kickmyb.serveur.utilisateur;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
public class ServiceUtilisateurImpl implements ServiceUtilisateur {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DepotUtilisateur userRepository;

    // This one has to be there by convention to fit with Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUtilisateur user = userRepository.findByUsername(username.trim().toLowerCase()).get();
        User u = new User(user.nom, user.motDePasse, new ArrayList<>());
        return u;
    }


    // https://stackoverflow.com/questions/36498327/catch-dataintegrityviolationexception-in-transactional-service-method
    @Override
    @Transactional(rollbackFor = NomDejaPris.class)
    public void inscrire(SignupRequest req) throws NomTropCourt, MotDePasseTropCourt, NomDejaPris {
        String username = req.username.toLowerCase().trim();
        if (username.length() < 2) throw new NomTropCourt();
        if (req.password.length() < 4) throw new MotDePasseTropCourt();
        // validation de l'unicité est faite au niveau de la BD voir MUser.java
        try {
            MUtilisateur p = new MUtilisateur();
            p.nom = username;
            p.motDePasse = passwordEncoder.encode(req.password);
            userRepository.saveAndFlush(p);
        } catch (DataIntegrityViolationException e) {
            throw new NomDejaPris();
        }
    }
}
