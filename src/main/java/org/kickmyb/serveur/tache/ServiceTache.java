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

    public static class Existant extends Exception {}
    public static class TropCourt extends Exception {}
    public static class Empty extends Exception {}

    @Autowired
    DepotUtilisateur repoUser;
    @Autowired
    DepotTache repo;
    @Autowired MProgressEventRepository repoProgressEvent;

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
        response.nom = element.name;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.pourcentageTemps = calculPourcentageTemps(element.dateCreation, new Date(), element.dateLimite);
        // aller chercher le dernier événement de progrès
        response.pourcentageAvancement = calculPourcentageAvancement(element);
        response.dateLimite = element.dateLimite;
        response.changements = new ArrayList<>();
        for (MProgressEvent e : element.events) {
            ChangementAvancement transfer = new ChangementAvancement();
            transfer.valeur = e.resultPercentage;
            transfer.dateChangement = e.timestamp;
            response.changements.add(transfer);
        }
        return response;
    }

    // TODO oublier de valider pour une injection javascript
    // TODO Que se passe-t-il si ce n'est pas transactionnel ()
    // TODO test unicité avec script de charge
    public void ajouteUneTache(RequeteAjoutTache req, Long idUtilisateur) throws Existant, Empty, TropCourt {
        MUtilisateur utilisateur = repoUser.findById(idUtilisateur).get();
        // valider que c'est non vide
        if (req.nom.trim().isEmpty()) throw new Empty();
        if (req.nom.trim().length() < 2) throw new TropCourt();
        // valider si le nom existe déjà
        for (MTache b : utilisateur.taches) {
            if (b.name.equalsIgnoreCase(req.nom)) throw new Existant();
        }
        // tout est beau, on crée
        MTache t = new MTache();
        t.name = req.nom;
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
        // TODO validate value is between 0 and 100
        MProgressEvent pe= new MProgressEvent();
        pe.resultPercentage = value;
        pe.completed = value ==100;
        pe.timestamp = DateTime.now().toDate();
        repoProgressEvent.save(pe);
        element.events.add(pe);
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
            r.nom = t.name;
            res.add(r);
        }
        return res;
    }

    private int calculPourcentageAvancement(MTache t) {
        return t.events.isEmpty()? 0 : t.events.get(t.events.size()-1).resultPercentage;
    }

    // TODO try to see how to make an injection attack example by directly exposing data from DB
    public String index() {
        String res = "<html>";
        res += "<div>Index :</div>";
        for (MUtilisateur u: repoUser.findAll()) {
            res += "<div>" + u.nom;
            for (MTache t : u.taches) {
                res += "<div>" + t.name  + "</div>";
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
            r.nom = t.name;
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
        MUtilisateur user = repoUser.findById(idUtilisateur).get();
        MTache element = user.taches.stream().filter(elt -> elt.id == id).findFirst().get();
        ReponseDetailTacheAvecPhoto response = new ReponseDetailTacheAvecPhoto();
        response.nom = element.name;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.pourcentageTemps = calculPourcentageTemps(element.dateCreation, new Date(), element.dateLimite);
        // aller chercher le dernier événement de progrès
        response.pourcentageAvancement = calculPourcentageAvancement(element);
        response.dateLimite = element.dateLimite;
        response.changements = new ArrayList<>();
        for (MProgressEvent e : element.events) {
            ChangementAvancement transfer = new ChangementAvancement();
            transfer.valeur = e.resultPercentage;
            transfer.dateChangement = e.timestamp;
            response.changements.add(transfer);
        }
        if(element.photo != null) {
            response.photoId = element.photo.id;
        } else {
            response.photoId = 0L;
        }
        return response;
    }

}
