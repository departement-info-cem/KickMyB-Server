package org.kickmyb.serveur.tache;

import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.transfer.ReponseAccueilItem;
import org.kickmyb.transfer.ReponseDetailTache;
import org.kickmyb.transfer.RequeteAjoutTache;
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

    @PostMapping(value = "/tache/ajout", produces = "text/plain")
    public @ResponseBody String ajout(@RequestBody RequeteAjoutTache requete) throws ServiceTache.Vide, ServiceTache.Existant, ServiceTache.TropCourt {
        System.out.println("KICKB SERVER : Ajout d'une tâche : " + requete.nom + ", date " + requete.dateLimite);
        MUtilisateur utilisateur = utilisateurDepuisCookie();
        serviceTache.ajouteUneTache(requete, utilisateur.id);
        return "";
    }

    @GetMapping(value = "/tache/progres/{idTache}/{valeur}", produces = "text/plain")
    public @ResponseBody String updateProgress(@PathVariable long idTache, @PathVariable int valeur) {
        System.out.println("KICKB SERVEUR : Mise à jour : " + idTache + " @" + valeur);
        serviceTache.miseAJourProgres(idTache, valeur);
        return "";
    }

    @GetMapping("/tache/accueil")
    public @ResponseBody List<ReponseAccueilItem> accueil() {
        MUtilisateur user = utilisateurDepuisCookie();
        System.out.println("KICKB SERVEUR : Liste des tâches pour l'utilisateur " + user.nom);
        return serviceTache.accueil(user.id);
    }

    @GetMapping("/tache/detail/{id}")
    public @ResponseBody ReponseDetailTache detail(@PathVariable long id) {
        System.out.println("KICKB SERVEUR : Détail  " + id);
        MUtilisateur user = utilisateurDepuisCookie();
        return serviceTache.detail(id, user.id);
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
