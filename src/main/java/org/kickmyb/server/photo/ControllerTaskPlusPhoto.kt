package org.kickmyb.server.photo

import org.kickmyb.server.ConfigHTTP
import org.kickmyb.server.account.MUser
import org.kickmyb.server.task.ServiceTask
import org.kickmyb.transfer.HomeItemPhotoResponse
import org.kickmyb.transfer.TaskDetailPhotoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class ControllerTaskPlusPhoto {
    @Autowired
    private lateinit var serviceTask: ServiceTask

    //Méthode utilisée pour récupérer les items de la liste des tâches avec des photos
    @GetMapping("/api/home/photo")
    @ResponseBody
    fun homePhoto(): List<HomeItemPhotoResponse> {
        println("KICKB SERVER : Task list  with cookie")
        ConfigHTTP.attenteArticifielle()
        val user = currentUser()
        return serviceTask.homePhoto(user.id)
    }

    //Méthode utilisée pour récupérer le détaild'une tâche avec des photos
    @GetMapping("/api/detail/photo/{id}")
    @ResponseBody
    fun detailPhoto(@PathVariable id: Long): TaskDetailPhotoResponse {
        println("KICKB SERVER : Detail  with cookie ")
        ConfigHTTP.attenteArticifielle()
        val user = currentUser()
        return serviceTask.detailPhoto(id, user)
    }

    private fun currentUser(): MUser {
        val ud = SecurityContextHolder.getContext().authentication.principal as UserDetails
        return serviceTask.userFromUsername(ud.username)
    }
}