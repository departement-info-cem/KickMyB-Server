package org.kickmyb.serveur.photo;

import jakarta.transaction.Transactional;
import org.kickmyb.serveur.tache.MTache;
import org.kickmyb.serveur.tache.DepotTache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service // Peut être remplacé par @Component mais c'est juste plus clair. Indique qu'il s'agit d'un bean, permet l'injection dans Autowired
public class ServicePhoto {

    // Spring va automatiquement chercher la classe annotée @Component ou @Repository qui correspond à ce type
    @Autowired private DepotPhoto depotPhoto;
    @Autowired private DepotTache depotTache;

    // TODO show what happens when transactional is removed by throwing an exception between writes??
    @Transactional
    public MPhoto stockerLeFichier(MultipartFile fichier, Long id) throws IOException {
        //String fileName = StringUtils.cleanPath(fichier.getOriginalFilename());
        MTache tache = depotTache.findById(id).get();
        try{
            MPhoto existant = depotPhoto.findByTache(tache).get();
            depotPhoto.delete(existant);
        } catch(Exception e){}
        // throw an exception here to show that transactional protects against delete but not store
        MPhoto photo = new MPhoto();
        photo.blob = fichier.getBytes();
        photo.typeContenu = fichier.getContentType();
        photo.tache = tache;
        photo = depotPhoto.save(photo);
        tache.photo = photo;
        depotTache.save(tache);
        return photo;
    }

    public MPhoto chargerFichier(Long id) {
        return depotPhoto.findById(id).get();
    }
}
