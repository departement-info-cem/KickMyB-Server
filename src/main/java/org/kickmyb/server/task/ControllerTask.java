package org.kickmyb.server.task;

import org.kickmyb.server.ConfigHTTP;
import org.kickmyb.server.account.MUser;
import org.kickmyb.server.exceptions.TaskNotFoundException;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO move to @AuthenticationPrincipal user

@Controller
public class ControllerTask {

    // explication de Autowired : Spring trouve automatiquement la classe annotée
    // @Component qui implémente l'interface
    @Autowired
    private ServiceTask serviceTask;

    @PostMapping(value = "/api/add", produces = "text/plain")
    public @ResponseBody String addOne(@RequestBody AddTaskRequest request) throws ServiceTask.Empty, ServiceTask.TooShort, ServiceTask.Existing {
        System.out.println("KICKB SERVER : Add a task : " + request.name + " date " + request.deadline);
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        serviceTask.addOne(request, user);
        return "";
    }

    @GetMapping(value = "/api/progress/{taskID}/{value}", produces = "text/plain")
    public @ResponseBody String updateProgress(@PathVariable long taskID, @PathVariable int value) {
        System.out.println("KICKB SERVER : Progress for task : " + taskID + " @" + value);
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        serviceTask.updateProgress(taskID, value, user);
        return "";
    }

    @GetMapping("/api/home")
    public @ResponseBody List<HomeItemResponse> home() {
        System.out.println("KICKB SERVER : Task list  with cookie");
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        return serviceTask.home(user.id);
    }

    @GetMapping("/api/detail/{id}")
    public @ResponseBody TaskDetailResponse detail(@PathVariable long id) {
        System.out.println("KICKB SERVER : Detail  with cookie ");
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        return serviceTask.detail(id, user);
    }

    // ControllerTask.java

    @DeleteMapping("/api/delete/{taskID}")
    public @ResponseBody String deleteTask(@PathVariable long taskID) throws TaskNotFoundException {
        System.out.println("KICKB SERVER : Delete task : " + taskID);
        ConfigHTTP.attenteArticifielle();
        MUser user = currentUser();
        serviceTask.delete(taskID, user);
        return "Task deleted";
    }



    /**
     * Accède au Principal stocké dans la mémoire vivre (HttpSession)
     * La session de l'utilisateur est accédée grâce au  JSESSIONID qui était dans lq requête dans un cookie
     * Ensuite, on va à la base de données pour récupérer l'objet user complet.
     */
    private MUser currentUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Le nom utilisateur est " + username);
        UserDetails ud = (UserDetails) authentication.getPrincipal();
        return serviceTask.userFromUsername(ud.getUsername());
    }
}
