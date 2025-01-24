package org.kickmyb.server.account;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.kickmyb.server.ConfigHTTP;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


// Modified for Spring Boot 3 https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
// signin signup and sign out in spring security

@Controller
public class ControllerAccount {

    // Spring security requires the AuthenticationManager to inject the security Context in the session
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private ServiceAccount userService;

    @Autowired
    private Gson gson;

    private @Autowired HttpServletRequest request;
    private @Autowired HttpServletResponse response;
    private @Autowired SecurityContextRepository securityContextRepository;

    @PostMapping("/api/id/signin")
    public @ResponseBody SigninResponse signin(@RequestBody SigninRequest s) throws BadCredentialsException {
        System.out.println("ID : SIGNIN request " + s);
        ConfigHTTP.attenteArticifielle();
        s.username = s.username.trim().toLowerCase();
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(s.username, s.password);
            // validate the authentication provided by the user
            auth = authManager.authenticate(auth);
            // attach the authentication in the session-backed security context
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, response);
            System.out.println("Logged as " + s.username);
            SigninResponse resp = new SigninResponse();
            resp.username = s.username;
            return resp;
        } catch (org.springframework.security.authentication.BadCredentialsException bce) {
            throw new BadCredentialsException();
        }
    }

    @PostMapping("/api/id/signup")
    public @ResponseBody SigninResponse signup(@RequestBody SignupRequest s) throws ServiceAccount.UsernameTooShort, ServiceAccount.PasswordTooShort, ServiceAccount.UsernameAlreadyTaken, BadCredentialsException {
        System.out.println("ID : SIGNUP request " + s);
        ConfigHTTP.attenteArticifielle();
        userService.signup(s);
        SigninRequest req = new SigninRequest();
        req.username = s.username;
        req.password = s.password;
        return signin(req);


    }

    @PostMapping(value = "/api/id/signout", produces = "plain/text")
    public @ResponseBody String signout() {
        System.out.println("ID : SIGNOUT REQUEST ");
        ConfigHTTP.attenteArticifielle();
        // clear the authentication in the session-based context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(null);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        return "";
    }
}
