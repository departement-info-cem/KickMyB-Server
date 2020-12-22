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

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

// This is JAX-RS Jersey style annotations
// Can be mixed with Spring Security security
// And can used Autowired Services too
@Path("/")
public class WebServiceCore {

	// explication de Autowired : Spring trouve automatiquement la classe annotée
	// @Component qui implémente l'interface
	@Autowired 		private Service service;

	@POST 		@Path("/addbaby")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addOne(AddTaskRequest request) throws Existing, BadCredentials {
		System.out.println("WS SOCIAL : add baby");
		MUser user = currentUser();
		service.addOne(request, user);
		return "";
	}

	@GET 		@Path("/home")
	@Produces(MediaType.APPLICATION_JSON)
	public List<HomeItemResponse> home() throws BadCredentials {
		System.out.println("WS SOCIAL : HOME REQUEST  with cookie" );
		MUser user = currentUser();
		return service.home(user.id);
	}

	@GET 		@Path("/index")
	@Produces(MediaType.TEXT_HTML)
	public String htmlIndex() {
		UserDetails ud = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		System.out.println("WS SOCIAL : INDEX  with cookie ::: " + ud.getUsername());
		return service.index();
	}

    @GET 		@Path("/detail/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaskDetailResponse detail(@PathParam("id") long itemID) throws BadCredentials {
		System.out.println("WS SOCIAL : DETAIL  with cookie " );
		MUser user = currentUser();
		return service.detail(itemID, user);
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
