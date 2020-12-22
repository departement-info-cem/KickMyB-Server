package org.kickmyb.server.id;

import org.kickmyb.server.exceptions.BadCredentials;


import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// Jersey based web service that provides endpoints for
// signin signup and signout in spring security

@Path("/id")
public class WebServiceID {

    // Spring security requires the AuthenticationManager to inject the security Context in the session
    @Autowired      private AuthenticationManager authManager;
    @Autowired      private ServiceID userService;

    @POST                  @Path("/signin")
    public SigninResponse signin(SigninRequest s) throws BadCredentials {
        System.out.println("ID : SIGNIN request " + s);
        s.username = s.username.trim().toLowerCase();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(s.username, s.password);
            // validate the authentication provided by the user
            authManager.authenticate(auth);
            // attach the authentication in the session-backed security context
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("Logged as " + s.username);
            SigninResponse resp = new SigninResponse();
            resp.username = s.username;
            return resp;
        } catch (BadCredentialsException bce) {
            throw new BadCredentials();
        }
    }

    @POST					@Path("/signup")
    public SigninResponse signup(SignupRequest s) throws BadCredentials {
        System.out.println("ID : SIGNUP request " + s);
        userService.signup(s);
        SigninRequest req = new SigninRequest();
        req.username = s.username;
        req.password = s.password;
        return signin(req);
    }

    @GET                    @Path("/signout")
    @Produces(MediaType.APPLICATION_JSON)
    public String signout() throws BadCredentials {
        System.out.println("ID : SIGNOUT REQUEST " );
        // clear the authentication in the session-based context
        SecurityContextHolder.getContext().setAuthentication(null);
        return "";
    }
}
