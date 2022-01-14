package org.kickmyb.server.account;

import com.google.gson.Gson;


import org.kickmyb.server.ConfigHTTP;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


// Jersey based web service that provides endpoints for
// signin signup and signout in spring security

//@Path("/id")
@Controller
public class ControllerAccount {

    // Spring security requires the AuthenticationManager to inject the security Context in the session
    @Autowired      private AuthenticationManager authManager;
    @Autowired      private ServiceAccount userService;

    @Autowired      private Gson gson;

    @PostMapping("/api/id/signin")
    public @ResponseBody SigninResponse signin(@RequestBody  SigninRequest s) throws BadCredentialsException {
        System.out.println("ID : SIGNIN request " + s);
        ConfigHTTP.attenteArticifielle();
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
        } catch (org.springframework.security.authentication.BadCredentialsException bce) {
            throw new BadCredentialsException();
        }
    }

    @PostMapping("/api/id/signup")
    public @ResponseBody SigninResponse signup(@RequestBody SignupRequest s)
            throws ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort,
            ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {
        System.out.println("ID : SIGNUP request " + s);
        ConfigHTTP.attenteArticifielle();
        userService.signup(s);
        SigninRequest req = new SigninRequest();
        req.username = s.username;
        req.password = s.password;
        return signin(req);


    }

    @PostMapping("/api/id/signout")
    public @ResponseBody String signout() throws BadCredentialsException {
        System.out.println("ID : SIGNOUT REQUEST " );
        ConfigHTTP.attenteArticifielle();
        // clear the authentication in the session-based context
        SecurityContextHolder.getContext().setAuthentication(null);
        return "";
    }
}
