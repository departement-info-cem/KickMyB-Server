package org.kickmyb.server.photo;

import org.imgscalr.Scalr;
import org.kickmyb.server.ConfigHTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller     // indique à Spring qu'il y a des points d'entrée dans la classe
public class ControllerPhoto {

    @Autowired private ServicePhoto servicePhoto;

    @PostMapping("/file")
    public ResponseEntity<String> up(@RequestParam("file") MultipartFile file, @RequestParam("babyID") Long babyID) throws IOException {
        System.out.println("PHOTO : upload request " + file.getContentType());
        ConfigHTTP.attenteArticifielle();
        servicePhoto.store(file, babyID);
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


    // TODO delete
    @GetMapping("/file/baby/{id}")
    public ResponseEntity<byte[]> compat(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        return taskPhoto(id, maxWidth);
    }

    @GetMapping("/file/task/{id}")
    public ResponseEntity<byte[]> taskPhoto(@PathVariable Long id, @RequestParam(required = false, name = "width") Integer maxWidth) throws IOException {
        System.out.println("PHOTO : download request " + id + " width " + maxWidth);
        ConfigHTTP.attenteArticifielle();
        MPhoto pic = servicePhoto.getFileForTask(id);
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
}
