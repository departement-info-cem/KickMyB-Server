package org.kickmyb.server.task;

import org.kickmyb.server.ConfigHTTP;
import org.kickmyb.server.account.MUser;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;
import org.kickmyb.transfer.HomeItemPhotoResponse;
import org.kickmyb.transfer.TaskDetailPhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This is JAX-RS Jersey style annotations
// Can be mixed with Spring Security security
// And can used Autowired Services too
@Controller
public class ControllerTask {

	// explication de Autowired : Spring trouve automatiquement la classe annotée
	// @Component qui implémente l'interface
	@Autowired 		private ServiceTask serviceTask;

	@PostMapping("/api/add")
	public @ResponseBody String addOne(@RequestBody AddTaskRequest request)
			throws ServiceTask.Empty, ServiceTask.TooShort, ServiceTask.Existing {
		System.out.println("KICKB SERVER : Add a task : " + request.name + " date " + request.deadline);
		ConfigHTTP.attenteArticifielle();
		MUser user = currentUser();
		serviceTask.addOne(request, user);
		return "";
	}

	@GetMapping("/api/progress/{taskID}/{value}")
	public @ResponseBody String updateProgress(
			@PathVariable long taskID,
			@PathVariable int value)  {
		System.out.println("KICKB SERVER : Progress for task : "+taskID + " @" + value);
		ConfigHTTP.attenteArticifielle();
		MUser user = currentUser();
		serviceTask.updateProgress(taskID,  value);
		return "";
	}

	@GetMapping("/api/home")
	public @ResponseBody List<HomeItemResponse> home() {
		System.out.println("KICKB SERVER : Task list  with cookie" );
		ConfigHTTP.attenteArticifielle();
		MUser user = currentUser();
		return serviceTask.home(user.id);
	}

    @GetMapping("/api/detail/{id}")
    public @ResponseBody TaskDetailResponse detail(@PathVariable long id) {
		System.out.println("KICKB SERVER : Detail  with cookie " );
		ConfigHTTP.attenteArticifielle();
		MUser user = currentUser();
		return serviceTask.detail(id, user);
    }

	/**
	 * Créer une page qui affiche tous les utilisateurs et les titres des tâches.
	 * @return
	 */
	@GetMapping("/index")
	public @ResponseBody String htmlIndex() {
		return serviceTask.index();
	}

	/**
	 * Tester votre serveur
	 * @return
	 */
	@GetMapping("/test")
	public @ResponseBody String test() {
		return "SALUT";
	}

	/**
	 * Accède au Principal stocké dans la mémoire vivre (HttpSession)
	 * La session de l'utilisateur est accédée grâce au  JSESSIONID qui était dans lq requête dans un cookie
	 * Ensuite on va à la base de données pour récupérer l'objet user complet.
	 * @return
	 */
	private MUser currentUser() {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MUser user = serviceTask.userFromUsername(ud.getUsername());
		return user;
	}


}
