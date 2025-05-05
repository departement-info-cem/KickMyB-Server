package org.kickmyb.serveur.utilisateur;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kickmyb.serveur.ConfigHTTP;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


// Modifié pour Spring Boot 3 https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html

@Controller
public class ControleurUtilisateur {

    // Spring security requires the AuthenticationManager to inject the security Context in the session
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private ServiceUtilisateur serviceUtilisateur;
    @Autowired
    private Gson gson;

    private @Autowired HttpServletRequest request;
    private @Autowired HttpServletResponse response;
    private @Autowired SecurityContextRepository securityContextRepository;

    @PostMapping("/id/connexion")
    public @ResponseBody ReponseConnexion connexion(@RequestBody RequeteConnexion s) throws ServiceUtilisateur.MauvaisNomOuMotDePasse {
        System.out.println("ID : Demande connexion " + s.nom);
        ConfigHTTP.attenteArticifielle();
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
            throw new ServiceUtilisateur.MauvaisNomOuMotDePasse();
        }
    }

    @PostMapping("/id/inscription")
    public @ResponseBody ReponseConnexion inscription(@RequestBody RequeteInscription s) throws ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt, ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MauvaisNomOuMotDePasse {
        System.out.println("ID : demande connexion " + s.nom);
        ConfigHTTP.attenteArticifielle();
        serviceUtilisateur.inscrire(s);
        RequeteConnexion req = new RequeteConnexion();
        req.nom = s.nom;
        req.motDePasse = s.motDePasse;
        return connexion(req);
    }

    @PostMapping(value = "/id/deconnexion", produces = "plain/text")
    public @ResponseBody String deconnexion() {
        System.out.println("ID : Demande déconnexion ");
        ConfigHTTP.attenteArticifielle();
        // clear the authentication in the session-based context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(null);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return "";
    }
}
