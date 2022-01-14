package org.kickmyb.server.account;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

// extends UserDetailsService which is one of the Spring Security entry points
public interface ServiceAccount extends UserDetailsService {

    class UsernameTooShort extends Exception {}
    class UsernameAlreadyTaken extends Exception {}
    class PasswordTooShort extends Exception {}

    void signup(SignupRequest req) throws BadCredentialsException, UsernameTooShort, PasswordTooShort, UsernameAlreadyTaken;

}
