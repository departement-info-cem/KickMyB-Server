package org.kickmyb.serveur.photo;

import org.imgscalr.Scalr;
import org.kickmyb.serveur.tache.ServiceTache;
import org.kickmyb.serveur.utilisateur.MUtilisateur;
import org.kickmyb.transfer.ReponseAccueilItemAvecPhoto;
import org.kickmyb.transfer.ReponseDetailTacheAvecPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller     // indique à Spring qu'il y a des points d'entrée dans la classe
public class ControleurPhoto {

    @Autowired
    private ServicePhoto servicePhoto;
    @Autowired
    private ServiceTache serviceTache;

    @PostMapping(value = "/fichier", produces = "text/plain")
    public ResponseEntity<String> televerser(@RequestParam("file") MultipartFile file, @RequestParam("taskID") Long taskID) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        MPhoto p = servicePhoto.stockerLeFichier(file, taskID);
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString());
    }

    @GetMapping("/fichier/{id}")
    public ResponseEntity<byte[]> photoPourLaTache(@PathVariable Long id, @RequestParam(required = false, name = "largeur") Integer largeurMax) throws IOException {
        System.out.println("PHOTO : requête téléchargement " + id + " largeur " + largeurMax);
        MPhoto pic = servicePhoto.chargerFichier(id);
        if (largeurMax == null) { // no resizing
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pic.blob);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(pic.blob);
            BufferedImage bi = ImageIO.read(bais);
            BufferedImage resized = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, largeurMax);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }
    }

    //Méthode utilisée pour récupérer les items de la liste des tâches avec des photos
    @GetMapping("/api/accueil/photo")
    public @ResponseBody List<ReponseAccueilItemAvecPhoto> accueilPhoto() {
        System.out.println("KICKB SERVER : liste de l'accueil avec photo");
        MUtilisateur user = utilisateurCourant();
        return serviceTache.homePhoto(user.id);
    }

    //Méthode utilisée pour récupérer le détail d'une tâche avec des photos
    @GetMapping("/api/detail/photo/{id}")
    public @ResponseBody ReponseDetailTacheAvecPhoto detailPhoto(@PathVariable long id) {
        System.out.println("KICKB SERVER : detail avec photo");
        MUtilisateur user = utilisateurCourant();
        return serviceTache.detailPhoto(id, user.id);
    }

    private MUtilisateur utilisateurCourant() {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return serviceTache.utilisateurParSonNom(ud.getUsername());
    }


}