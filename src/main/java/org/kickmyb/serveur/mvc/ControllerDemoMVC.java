package org.kickmyb.serveur.mvc;

import org.kickmyb.serveur.tache.ServiceTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class ControllerDemoMVC {

    @Autowired
    ServiceTache serviceTache;

    // si le type de retour est String, alors le nom de la vue est retourné
    // la vue se retrouve dans le dossier src/main/resources/templates
    // il s'agit d'un template thymeleaf
    // on lui passe un objet Model qui contient des attributs
    @GetMapping("/")
    public String index(Model modèle) {
        // plein de calculs ici qui implique la BD des appels serveurs tiers
        // finalement on charge un gabarit thymeleaf qui reçoit le modèle
        modèle.addAttribute("message", "Salut le monde!");
        return "index";
    }

    /**
     * Créer une page qui affiche tous les utilisateurs et les titres des tâches.
     */
    @GetMapping(value = "/index", produces = "text/html")
    public @ResponseBody String htmlIndex() {
        return serviceTache.index();
    }

}
