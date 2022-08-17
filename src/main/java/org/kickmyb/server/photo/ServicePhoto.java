package org.kickmyb.server.photo;

import org.kickmyb.server.task.MTask;
import org.kickmyb.server.task.MTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service // Peut être remplacé par @Component mais c'est juste plus clair. Indique qu'il s'agit d'un bean, permet l'injection dans Autowired
public class ServicePhoto {

    // Spring va automatiquement chercher la classe annotée @Component ou @Repository qui correspond à ce type
    @Autowired private MPhotoRepository repoPics;
    @Autowired private MTaskRepository repo;

    // TODO show what happens when transactional is removed by throwing an exception between writes??
    @Transactional
    public MPhoto store(MultipartFile file, Long elementID) throws IOException {
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        MTask task = repo.findById(elementID).get();
        try{
            MPhoto existing = repoPics.findByTask(task).get();
            repoPics.delete(existing);
        } catch(Exception e){}
        // throw an exception here to show that transactional protects against delete but not store
        MPhoto photo = new MPhoto();
        photo.blob = file.getBytes();
        photo.contentType = file.getContentType();
        photo.task = task;

        photo = repoPics.save(photo);

        task.photo = photo;
        repo.save(task);

        return photo;
    }

    @Transactional
    public MPhoto storeSingle(MultipartFile file) throws IOException {

        MPhoto photo = new MPhoto();
        photo.blob = file.getBytes();
        photo.contentType = file.getContentType();

        photo = repoPics.save(photo);

        return photo;
    }

    /*public MPhoto getFileForTask(Long elementID) {
        MTask element = repo.findById(elementID).get();
        return repoPics.findByTask(element).get();
    }*/

    public MPhoto getFile(Long elementID) {
        return repoPics.findById(elementID).get();
    }
}
