package org.kickmyb.server.tasks;

import org.kickmyb.server.model.MUser;
import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.TaskDetailResponse;
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
public class ControllerCore {

	// explication de Autowired : Spring trouve automatiquement la classe annotée
	// @Component qui implémente l'interface
	@Autowired 		private ServiceCore serviceCore;

	@PostMapping("/api/add")
	public @ResponseBody String addOne(@RequestBody AddTaskRequest request) throws Existing {
		System.out.println("WS SOCIAL : add baby");
		MUser user = currentUser();
		serviceCore.addOne(request, user);
		return "";
	}

	@GetMapping("/api/home")
	public @ResponseBody List<HomeItemResponse> home() {
		System.out.println("WS SOCIAL : HOME REQUEST  with cookie" );
		MUser user = currentUser();
		return serviceCore.home(user.id);
	}

	@GetMapping("/index")
	public @ResponseBody String htmlIndex() {
		try {
			UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			System.out.println("WS SOCIAL : INDEX  with cookie ::: " + ud.getUsername());
		} catch(Exception e) {
			e.printStackTrace();
		}
		return serviceCore.index();
	}

    @GetMapping("/api/detail/{id}")
    public @ResponseBody TaskDetailResponse detail(@PathVariable long id) {
		System.out.println("WS SOCIAL : DETAIL  with cookie " );
		MUser user = currentUser();
		return serviceCore.detail(id, user);
    }

	/**
	 * Gets the Principal from the in RAM HttpSession looked up from the JSESSIONID sent via cookie
	 * Goes to database
	 * @return
	 */
	private MUser currentUser() {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MUser user = serviceCore.userFromUsername(ud.getUsername());
		return user;
	}

}
