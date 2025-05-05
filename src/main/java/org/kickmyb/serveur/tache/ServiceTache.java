package org.kickmyb.serveur.tache;

import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.transfer.*;

import java.util.List;

public interface ServiceTache {

    class Existant extends Exception {}
    class TropCourt extends Exception {}
    class Empty extends Exception {}

    // entity handling
    TaskDetailResponse detail(Long idTache, Long idUtilisateur);
    void ajouteUneTache(AddTaskRequest req, Long idUtilisateur) throws Existant, Empty, TropCourt;
    void miseAJourProgres(long taskID, int value);
    List<HomeItemResponse> accueil(Long userID);
    TaskDetailPhotoResponse detailPhoto(Long id, Long idUtilisateur);
    List<HomeItemPhotoResponse> homePhoto(Long idUtilisateur);

    // Potential web demo for JS injection
    String index();
    MUtilisateur utilisateurParSonNom(String nomUtilisateur);
}