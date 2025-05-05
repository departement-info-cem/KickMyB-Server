package org.kickmyb.serveur.tache;

import org.kickmyb.serveur.ConfigHTTP;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO move to @AuthenticationPrincipal user

@Controller
public class ControleurTache {

    // explication de Autowired : Spring trouve automatiquement la classe annotée
    // @Component qui implémente l'interface
    @Autowired
    private ServiceTache serviceTache;

    @PostMapping(value = "/api/add", produces = "text/plain")
    public @ResponseBody String ajout(@RequestBody AddTaskRequest request) throws ServiceTache.Empty, ServiceTache.TropCourt, ServiceTache.Existant {
        System.out.println("KICKB SERVER : Add a task : " + request.name + " date " + request.deadline);
        ConfigHTTP.attenteArticifielle();
        MUtilisateur user = utilisateurDepuisCookie();
        serviceTache.ajouteUneTache(request, user);
        return "";
    }

    @GetMapping(value = "/api/progress/{taskID}/{value}", produces = "text/plain")
    public @ResponseBody String updateProgress(@PathVariable long idTache, @PathVariable int valeur) {
        System.out.println("KICKB SERVEUR : Mise à jour : " + idTache + " @" + valeur);
        ConfigHTTP.attenteArticifielle();
        serviceTache.miseAJourProgres(idTache, valeur);
        return "";
    }

    @GetMapping("/tache/accueil")
    public @ResponseBody List<HomeItemResponse> accueil() {
        System.out.println("KICKB SERVEUR : Liste des tâches cookie");
        ConfigHTTP.attenteArticifielle();
        MUtilisateur user = utilisateurDepuisCookie();
        return serviceTache.accueil(user.id);
    }

    @GetMapping("/tache/detail/{id}")
    public @ResponseBody TaskDetailResponse detail(@PathVariable long id) {
        System.out.println("KICKB SERVEUR : Détail  " + id);
        ConfigHTTP.attenteArticifielle();
        MUtilisateur user = utilisateurDepuisCookie();
        return serviceTache.detail(id, user);
    }

    /**
     * Accède au Principal stocké dans la mémoire vivre (HttpSession)
     * La session de l'utilisateur est accédée grâce au  JSESSIONID qui était dans lq requête dans un cookie
     * Ensuite, on va à la base de données pour récupérer l'objet user complet.
     */
    private MUtilisateur utilisateurDepuisCookie() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Le nom utilisateur est " + authentication.getName());
        UserDetails ud = (UserDetails) authentication.getPrincipal();
        return serviceTache.utilisateurParSonNom(ud.getUsername());
    }
}
