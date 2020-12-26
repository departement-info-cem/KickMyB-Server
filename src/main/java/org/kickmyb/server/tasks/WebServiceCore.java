package org.kickmyb.server.tasks;

import org.kickmyb.server.exceptions.BadCredentials;
import org.kickmyb.server.exceptions.Existing;
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
public class WebServiceCore {

	// explication de Autowired : Spring trouve automatiquement la classe annotée
	// @Component qui implémente l'interface
	@Autowired 		private Service service;

	@PostMapping("/api/addbaby")
	public @ResponseBody String addOne(@RequestBody AddTaskRequest request) throws Existing, BadCredentials {
		System.out.println("WS SOCIAL : add baby");
		MUser user = currentUser();
		service.addOne(request, user);
		return "";
	}

	@GetMapping("/api/home")
	public @ResponseBody List<HomeItemResponse> home() throws BadCredentials {
		System.out.println("WS SOCIAL : HOME REQUEST  with cookie" );
		MUser user = currentUser();
		return service.home(user.id);
	}

	@GetMapping("/index")
	public @ResponseBody String htmlIndex() {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("WS SOCIAL : INDEX  with cookie ::: " + ud.getUsername());
		return service.index();
	}

    @GetMapping("/api/detail/{id}")
    public @ResponseBody TaskDetailResponse detail(@PathVariable long id) throws BadCredentials {
		System.out.println("WS SOCIAL : DETAIL  with cookie " );
		MUser user = currentUser();
		return service.detail(id, user);
    }

	/**
	 * Gets the Principal from the in RAM HttpSession looked up from the JSESSIONID sent via cookie
	 * Goes to database
	 * @return
	 */
	private MUser currentUser() {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MUser user = service.userFromUsername(ud.getUsername());
		return user;
	}

}
