package org.kickmyb.server.photo

import org.imgscalr.Scalr
import org.kickmyb.server.ConfigHTTP
import org.kickmyb.server.account.MUser
import org.kickmyb.server.task.ServiceTask
import org.kickmyb.transfer.HomeItemPhotoResponse
import org.kickmyb.transfer.TaskDetailPhotoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.imageio.ImageIO

@Controller // indique à Spring qu'il y a des points d'entrée dans la classe

// TODO faire du logging comme un pro
class ControllerPhoto {
    @Autowired private lateinit var servicePhoto: ServicePhoto
    @Autowired private lateinit var serviceTask: ServiceTask
    @PostMapping("/file")
    //@Throws(IOException::class)
    fun up(@RequestParam("file") file: MultipartFile, @RequestParam("taskID") taskID: Long?): ResponseEntity<String> {
        println("PHOTO : upload request " + file.contentType)
        ConfigHTTP.attenteArticifielle()
        val p = servicePhoto.store(file, taskID)
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString())
    }

    @GetMapping("/file/{id}")
    //@Throws(IOException::class)
    fun taskPhoto(@PathVariable id: Long, @RequestParam(required = false, name = "width") maxWidth: Int?): ResponseEntity<ByteArray> {
        println("PHOTO : download request $id width $maxWidth")
        ConfigHTTP.attenteArticifielle()
        val pic = servicePhoto.getFile(id)
        // TODO explain resizing logic
        return if (maxWidth == null) { // no resizing
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pic.blob)
        } else {
            val bais = ByteArrayInputStream(pic.blob)
            val bi = ImageIO.read(bais)
            val resized = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, maxWidth)
            val baos = ByteArrayOutputStream()
            ImageIO.write(resized, "jpg", baos)
            val bytes = baos.toByteArray()
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes)
        }
    }

    //Méthode utilisée uniquement pour les exercices
    @PostMapping("/singleFile")
    //@Throws(IOException::class)
    fun upSingle(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        println("PHOTO : single upload request " + file.contentType)
        ConfigHTTP.attenteArticifielle()
        val p = servicePhoto.storeSingle(file)
        return ResponseEntity.status(HttpStatus.OK).body(p.id.toString())
    }

    //Méthode utilisée uniquement pour les exercices
    @GetMapping("/singleFile/{id}")
    //@Throws(IOException::class)
    fun photoSingle(@PathVariable id: Long, @RequestParam(required = false, name = "width") maxWidth: Int?): ResponseEntity<ByteArray> {
        return taskPhoto(id, maxWidth)
    }

    //Méthode utilisée uniquement pour les exercices
    @PostMapping("/api/singleFile")
    //@Throws(IOException::class)
    fun upSingleCookie(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        println("PHOTO : cookie " + file.contentType)
        return upSingle(file)
    }

    //Méthode utilisée uniquement pour les exercices
    @GetMapping("/api/singleFile/{id}")
    //@Throws(IOException::class)
    fun photoSingleCookie(@PathVariable id: Long, @RequestParam(required = false, name = "width") maxWidth: Int?): ResponseEntity<ByteArray> {
        return taskPhoto(id, maxWidth)
    }

    private fun currentUser(): MUser {
        val ud = SecurityContextHolder.getContext().authentication.principal as UserDetails
        return serviceTask.userFromUsername(ud.username)
    }
}
