package org.kickmyb.serveur.tache;

import org.joda.time.DateTime;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.serveur.utilisateur.DepotUtilisateur;
import org.kickmyb.transfer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ServiceTache  {

    public static class Existant extends RuntimeException {}
    public static class TropCourt extends RuntimeException {}
    public static class TropLong extends RuntimeException {}
    public static class Vide extends RuntimeException {}

    @Autowired
    DepotUtilisateur repoUser;
    @Autowired
    DepotTache repo;
    @Autowired
    MAvancementRepository repoProgressEvent;

    private int calculPourcentageTemps(Date start, Date current, Date end){
        if (current.after(end)) return 100;
        long total = end.getTime() - start.getTime();
        long spent = current.getTime() - start.getTime();
        double percentage =  100.0 * spent / total;
        // TODO si end est avant start c'est tout cassé.
        return Math.max((int) percentage, 0 );
    }

    public ReponseDetailTache detail(Long id, Long idUtilisateur) {
        MUtilisateur user = repoUser.findById(idUtilisateur).get();
        //MTask element = user.tasks.stream().filter(elt -> elt.id == id).findFirst().get();
        MTache element = repo.findById(id).get();
        ReponseDetailTache response = new ReponseDetailTache();
        response.nom = element.nom;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.pourcentageTemps = calculPourcentageTemps(element.dateCreation, new Date(), element.dateLimite);
        // aller chercher le dernier événement de progrès
        response.pourcentageAvancement = calculPourcentageAvancement(element);
        response.dateLimite = element.dateLimite;
        response.changements = new ArrayList<>();
        for (MAvancement e : element.avancements) {
            ChangementAvancement transfer = new ChangementAvancement();
            transfer.valeur = e.nouveauPourcentage;
            transfer.dateChangement = e.date;
            response.changements.add(transfer);
        }
        return response;
    }

    // TODO oublier de valider pour une injection javascript
    // TODO Que se passe-t-il si ce n'est pas transactionnel ()
    // TODO test unicité avec script de charge
    public void ajouteUneTache(RequeteAjoutTache req, Long idUtilisateur) {
        MUtilisateur utilisateur = repoUser.findById(idUtilisateur).get();
        // valider que c'est non vide
        if (req.nom.trim().isEmpty()) throw new Vide();
        if (req.nom.trim().length() < 2) throw new TropCourt();
        if (req.nom.trim().length() > 255) throw new TropLong();
        // valider si le nom existe déjà
        for (MTache b : utilisateur.taches) {
            if (b.nom.equalsIgnoreCase(req.nom)) throw new Existant();
        }
        // tout est beau, on crée
        MTache t = new MTache();
        t.nom = req.nom;
        t.dateCreation = DateTime.now().toDate();
        if (req.dateLimite == null) {
            t.dateLimite = DateTime.now().plusDays(7).toDate();
        } else {
            t.dateLimite = req.dateLimite;
        }
        repo.save(t);
        utilisateur.taches.add(t);
        repoUser.save(utilisateur);
    }

    public void miseAJourProgres(long taskID, int value) {
        MTache element = repo.findById(taskID).get();
        if (value < 0 || value > 100) {
            // TODO pourquoi être cohérent sur les exceptions quand on peut ne pas l'être
            throw new IllegalArgumentException("Valeur entre 0 et 100");
        }
        MAvancement pe= new MAvancement();
        pe.nouveauPourcentage = value;
        pe.date = DateTime.now().toDate();
        repoProgressEvent.save(pe);
        element.avancements.add(pe);
        repo.save(element);
    }

    public List<ReponseAccueilItem> accueil(Long userID) {
        MUtilisateur user = repoUser.findById(userID).get();
        List<ReponseAccueilItem> res = new ArrayList<>();
        for (MTache t : user.taches) {
            ReponseAccueilItem r = new ReponseAccueilItem();
            r.id = t.id;
            r.pourcentageAvancement = calculPourcentageAvancement(t);
            r.dateLimite = t.dateLimite;
            r.pourcentageTemps = calculPourcentageTemps(t.dateCreation, new Date(), t.dateLimite);
            r.nom = t.nom;
            res.add(r);
        }
        return res;
    }

    private int calculPourcentageAvancement(MTache t) {
        return t.avancements.isEmpty()? 0 : t.avancements.getLast().nouveauPourcentage;
    }

    // TODO permet de faire une injection javascript parce qu'on concatène des trucs fournis par l'utilisateur
    public String index() {
        String res = "<html>";
        res += "<div>Index :</div>";
        for (MUtilisateur u: repoUser.findAll()) {
            res += "<div>" + u.nom;
            for (MTache t : u.taches) {
                res += "<div>" + t.nom + "</div>";
            }
            res += "</div>";
        }
        res += "</html>";
        return res;
    }

    public MUtilisateur utilisateurParSonNom(String username) {
        return repoUser.findByNom(username).get();
    }

    public List<ReponseAccueilItemAvecPhoto> homePhoto(Long userID) {
        MUtilisateur user = repoUser.findById(userID).get();
        List<ReponseAccueilItemAvecPhoto> res = new ArrayList<>();
        for (MTache t : user.taches) {
            ReponseAccueilItemAvecPhoto r = new ReponseAccueilItemAvecPhoto();
            r.id = t.id;
            r.pourcentageAvancement = calculPourcentageAvancement(t);
            r.dateLimite = t.dateLimite;
            r.pourcentageTemps = calculPourcentageTemps(t.dateCreation, new Date(), t.dateLimite);
            r.nom = t.nom;
            if(t.photo != null) {
                r.idPhoto = t.photo.id;
            } else {
                r.idPhoto = 0L;
            }
            res.add(r);
        }
        return res;
    }

    public ReponseDetailTacheAvecPhoto detailPhoto(Long id, Long idUtilisateur) {
        MUtilisateur utilisateur = repoUser.findById(idUtilisateur).get();
        MTache element = utilisateur.taches.stream().filter(elt -> elt.id == id).findFirst().get();
        ReponseDetailTacheAvecPhoto reponse = new ReponseDetailTacheAvecPhoto();
        reponse.nom = element.nom;
        reponse.id = element.id;
        // calcul le temps écoulé en pourcentage
        reponse.pourcentageTemps = calculPourcentageTemps(element.dateCreation, new Date(), element.dateLimite);
        // aller chercher le dernier événement de progrès
        reponse.pourcentageAvancement = calculPourcentageAvancement(element);
        reponse.dateLimite = element.dateLimite;
        reponse.changements = new ArrayList<>();
        for (MAvancement e : element.avancements) {
            ChangementAvancement transfer = new ChangementAvancement();
            transfer.valeur = e.nouveauPourcentage;
            transfer.dateChangement = e.date;
            reponse.changements.add(transfer);
        }
        if(element.photo != null) {
            reponse.photoId = element.photo.id;
        } else {
            reponse.photoId = 0L;
        }
        return reponse;
    }

}
