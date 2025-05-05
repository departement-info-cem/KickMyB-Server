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
public class ServiceTacheImpl implements ServiceTache {

    @Autowired
    DepotUtilisateur repoUser;
    @Autowired
    DepotTache repo;
    @Autowired MProgressEventRepository repoProgressEvent;

    private int percentage(Date start, Date current, Date end){
        if (current.after(end)) return 100;
        long total = end.getTime() - start.getTime();
        long spent = current.getTime() - start.getTime();
        double percentage =  100.0 * spent / total;
        // TODO si end est avant start c'est tout cassé.
        return Math.max((int) percentage, 0 );
    }

    @Override
    public TaskDetailResponse detail(Long id, Long idUtilisateur) {
        MUtilisateur user = repoUser.findById(idUtilisateur).get();
        //MTask element = user.tasks.stream().filter(elt -> elt.id == id).findFirst().get();
        MTache element = repo.findById(id).get();
        TaskDetailResponse response = new TaskDetailResponse();
        response.name = element.name;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.percentageTimeSpent = percentage(element.creationDate, new Date(), element.deadline);
        // aller chercher le dernier événement de progrès
        response.percentageDone = percentageDone(element);
        response.deadline = element.deadline;
        response.events = new ArrayList<>();
        for (MProgressEvent e : element.events) {
            ProgressEvent transfer = new ProgressEvent();
            transfer.value = e.resultPercentage;
            transfer.timestamp = e.timestamp;
            response.events.add(transfer);
        }
        return response;
    }

    // TODO oublier de valider pour une injection javascript
    // TODO Que se passe-t-il si ce n'est pas transactionnel ()
    // TODO test unicité avec script de charge
    @Override
    public void ajouteUneTache(AddTaskRequest req, Long idUtilisateur) throws Existant, Empty, TropCourt {
        MUtilisateur utilisateur = repoUser.findById(idUtilisateur).get();
        // valider que c'est non vide
        if (req.name.trim().isEmpty()) throw new Empty();
        if (req.name.trim().length() < 2) throw new TropCourt();
        // valider si le nom existe déjà
        for (MTache b : utilisateur.taches) {
            if (b.name.equalsIgnoreCase(req.name)) throw new Existant();
        }
        // tout est beau, on crée
        MTache t = new MTache();
        t.name = req.name;
        t.creationDate = DateTime.now().toDate();
        if (req.deadline == null) {
            t.deadline = DateTime.now().plusDays(7).toDate();
        } else {
            t.deadline = req.deadline;
        }
        repo.save(t);
        utilisateur.taches.add(t);
        repoUser.save(utilisateur);
    }

    @Override
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

    @Override
    public List<HomeItemResponse> accueil(Long userID) {
        MUtilisateur user = repoUser.findById(userID).get();
        List<HomeItemResponse> res = new ArrayList<>();
        for (MTache t : user.taches) {
            HomeItemResponse r = new HomeItemResponse();
            r.id = t.id;
            r.percentageDone = percentageDone(t);
            r.deadline = t.deadline;
            r.percentageTimeSpent = percentage(t.creationDate, new Date(), t.deadline);
            r.name = t.name;
            res.add(r);
        }
        return res;
    }

    private int percentageDone(MTache t) {
        return t.events.isEmpty()? 0 : t.events.get(t.events.size()-1).resultPercentage;
    }

    // TODO try to see how to make an injection attack example by directly exposing data from DB
    @Override
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

    @Override
    public MUtilisateur utilisateurParSonNom(String username) {
        return repoUser.findByUsername(username).get();
    }

    @Override
    public List<HomeItemPhotoResponse> homePhoto(Long userID) {
        MUtilisateur user = repoUser.findById(userID).get();
        List<HomeItemPhotoResponse> res = new ArrayList<>();
        for (MTache t : user.taches) {
            HomeItemPhotoResponse r = new HomeItemPhotoResponse();
            r.id = t.id;
            r.percentageDone = percentageDone(t);
            r.deadline = t.deadline;
            r.percentageTimeSpent = percentage(t.creationDate, new Date(), t.deadline);
            r.name = t.name;
            if(t.photo != null) {
                r.photoId = t.photo.id;
            } else {
                r.photoId = 0L;
            }
            res.add(r);
        }
        return res;
    }

    @Override
    public TaskDetailPhotoResponse detailPhoto(Long id, Long idUtilisateur) {
        MUtilisateur user = repoUser.findById(idUtilisateur).get();
        MTache element = user.taches.stream().filter(elt -> elt.id == id).findFirst().get();
        TaskDetailPhotoResponse response = new TaskDetailPhotoResponse();
        response.name = element.name;
        response.id = element.id;
        // calcul le temps écoulé en pourcentage
        response.percentageTimeSpent = percentage(element.creationDate, new Date(), element.deadline);
        // aller chercher le dernier événement de progrès
        response.percentageDone = percentageDone(element);
        response.deadline = element.deadline;
        response.events = new ArrayList<>();
        for (MProgressEvent e : element.events) {
            ProgressEvent transfer = new ProgressEvent();
            transfer.value = e.resultPercentage;
            transfer.timestamp = e.timestamp;
            response.events.add(transfer);
        }
        if(element.photo != null) {
            response.photoId = element.photo.id;
        } else {
            response.photoId = 0L;
        }
        return response;
    }

}
