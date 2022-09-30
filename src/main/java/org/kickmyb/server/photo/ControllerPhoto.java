package org.kickmyb.server.photo;

import org.imgscalr.Scalr;
import org.kickmyb.server.ConfigHTTP;
import org.kickmyb.server.account.MUser;
import org.kickmyb.server.task.ServiceTask;
import org.kickmyb.transfer.HomeItemPhotoResponse;
import org.kickmyb.transfer.TaskDetailPhotoResponse;
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
public class ControllerPhoto {

    @Autowired private ServicePhoto servicePhoto;
    @Autowired 		private ServiceTask serviceTask;

    @PostMapping("/file")
    public ResponseEntity<String> up(@RequestParam("file") MultipartFile file, @RequestParam("taskID") Long taskID) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        ConfigHTTP.attenteArticifielle();
        MPhoto p = servicePhoto.store(file, taskID);
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString());
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> taskPhoto(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request " + id + " width " + maxWidth);
        ConfigHTTP.attenteArticifielle();
        MPhoto pic = servicePhoto.getFile(id);
        // TODO explain resizing logic
        if (maxWidth == null) { // no resizing
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pic.blob);
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(pic.blob);
            BufferedImage bi = ImageIO.read(bais);
            BufferedImage resized = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY,  maxWidth);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }
    }

    //Méthode utilisée uniquement pour les exercices
    @PostMapping("/singleFile")
    public ResponseEntity<String> upSingle(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("PHOTO : single upload request " + file.getContentType());
        ConfigHTTP.attenteArticifielle();
        MPhoto p = servicePhoto.storeSingle(file);
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString());
    }

    //Méthode utilisée uniquement pour les exercices
    @GetMapping("/singleFile/{id}")
    public ResponseEntity<byte[]> photoSingle(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        return taskPhoto(id, maxWidth);
    }

    //Méthode utilisée uniquement pour les exercices
    @PostMapping("/api/singleFile")
    public ResponseEntity<String> upSingleCookie(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("PHOTO : cookie " + file.getContentType());
        return upSingle(file);
    }

    //Méthode utilisée uniquement pour les exercices
    @GetMapping("/api/singleFile/{id}")
    public ResponseEntity<byte[]> photoSingleCookie(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        return taskPhoto(id, maxWidth);
    }

    //Méthode utilisée pour récupérer les items de la liste des tâches avec des photos
    @GetMapping("/api/home/photo")
    public @ResponseBody List<HomeItemPhotoResponse> homePhoto() {
        System.out.println("KICKB SERVER : Task list  with cookie" );
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        return serviceTask.homePhoto(user.id);
    }

    //Méthode utilisée pour récupérer le détaild'une tâche avec des photos
    @GetMapping("/api/detail/photo/{id}")
    public @ResponseBody
    TaskDetailPhotoResponse detailPhoto(@PathVariable long id) {
        System.out.println("KICKB SERVER : Detail  with cookie " );
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        return serviceTask.detailPhoto(id, user);
    }

    private MUser currentUser() {
        UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MUser user = serviceTask.userFromUsername(ud.getUsername());
        return user;
    }


}
