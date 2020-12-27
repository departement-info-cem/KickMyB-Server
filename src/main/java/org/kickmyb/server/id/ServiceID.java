package org.kickmyb.server.id;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

// extends UserDetailsService which is one of the Spring Security entry points
public interface ServiceID extends UserDetailsService {

    void signup(SignupRequest req) throws BadCredentials;

}
