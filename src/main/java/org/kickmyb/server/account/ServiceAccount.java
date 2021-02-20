package org.kickmyb.server.account;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

// extends UserDetailsService which is one of the Spring Security entry points
public interface ServiceAccount extends UserDetailsService {

    void signup(SignupRequest req) throws BadCredentialsException;

}
