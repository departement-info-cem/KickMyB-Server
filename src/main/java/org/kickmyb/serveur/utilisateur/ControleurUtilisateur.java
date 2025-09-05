package org.kickmyb.serveur.utilisateur;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kickmyb.transfer.ReponseConnexion;
import org.kickmyb.transfer.RequeteConnexion;
import org.kickmyb.transfer.RequeteInscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


// Modifié pour Spring Boot 3 https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html

@Controller
public class ControleurUtilisateur {

    // Spring security requires the AuthenticationManager to inject the security Context in the session
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private ServiceUtilisateur serviceUtilisateur;

    private @Autowired HttpServletRequest request;
    private @Autowired HttpServletResponse response;
    private @Autowired SecurityContextRepository securityContextRepository;

    @PostMapping("/id/connexion")
    public @ResponseBody ReponseConnexion connexion(@RequestBody RequeteConnexion s) {
        System.out.println("ID : Demande connexion " + s.nom);
        s.nom = s.nom.trim().toLowerCase();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(s.nom, s.motDePasse);
            auth = authManager.authenticate(auth);
            // attache l'authentification au contexte de sécurité
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            System.out.println("Logged as " + s.nom);
            ReponseConnexion resp = new ReponseConnexion();
            resp.nomUtilisateur = s.nom;
            return resp;
        } catch (org.springframework.security.authentication.BadCredentialsException bce) {
            // TODO valider qu'on passe ici ou pas?
            throw new ServiceUtilisateur.MauvaisNomOuMotDePasse();
        }
    }

    @PostMapping("/id/inscription")
    public @ResponseBody ReponseConnexion inscription(@RequestBody RequeteInscription s) {
        System.out.println("ID : demande connexion " + s.nom);
        serviceUtilisateur.inscrire(s);
        RequeteConnexion req = new RequeteConnexion();
        req.nom = s.nom;
        req.motDePasse = s.motDePasse;
        return connexion(req);
    }

    @PostMapping(value = "/id/deconnexion", produces = "plain/text")
    public @ResponseBody String deconnexion() {
        System.out.println("ID : Demande déconnexion ");
        // supprime le contexte de sécurité de la session
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(null);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return "";
    }

    @PostMapping(value = "/enregistrer-jeton-notification", produces = "plain/text")
    public @ResponseBody String enregistrerJetonNotification(@RequestBody String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String nomUtilisateur = auth.getName();
            serviceUtilisateur.enregistrerTokenFirebase(nomUtilisateur, token);
            System.out.println("Token Firebase enregistré pour l'utilisateur : " + nomUtilisateur);
            return "TokenEnregistre";
        }
        return "NonAuthentifie";
    }
}
