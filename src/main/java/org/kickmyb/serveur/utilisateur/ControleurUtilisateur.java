package org.kickmyb.serveur.utilisateur;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kickmyb.serveur.ConfigHTTP;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
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
    public @ResponseBody SigninResponse connexion(@RequestBody SigninRequest s) throws ServiceUtilisateur.MauvaisNomOuMotDePasse {
        System.out.println("ID : Demande connexion " + s.username);
        ConfigHTTP.attenteArticifielle();
        s.username = s.username.trim().toLowerCase();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(s.username, s.password);
            auth = authManager.authenticate(auth);
            // attache l'authentification au contexte de sécurité
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            System.out.println("Logged as " + s.username);
            SigninResponse resp = new SigninResponse();
            resp.username = s.username;
            return resp;
        } catch (org.springframework.security.authentication.BadCredentialsException bce) {
            throw new ServiceUtilisateur.MauvaisNomOuMotDePasse();
        }
    }

    @PostMapping("/id/inscription")
    public @ResponseBody SigninResponse inscription(@RequestBody SignupRequest s) throws ServiceUtilisateur.NomTropCourt, ServiceUtilisateur.MotDePasseTropCourt, ServiceUtilisateur.NomDejaPris, ServiceUtilisateur.MauvaisNomOuMotDePasse {
        System.out.println("ID : demande connexion " + s.username);
        ConfigHTTP.attenteArticifielle();
        serviceUtilisateur.inscrire(s);
        SigninRequest req = new SigninRequest();
        req.username = s.username;
        req.password = s.password;
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
