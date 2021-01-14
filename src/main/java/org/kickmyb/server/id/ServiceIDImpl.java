package org.kickmyb.server.id;

import org.kickmyb.server.model.MUser;
import org.kickmyb.server.model.MUserRepository;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Component
@Transactional
public class ServiceIDImpl implements ServiceID {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MUserRepository userRepository;

    // This one has to be there by convention to fit with Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MUser user = userRepository.findByUsername(username.trim().toLowerCase()).get();
        User u = new User(user.username, user.password, new ArrayList<>());
        return u;
    }

    @Override
    public void signup(SignupRequest req) throws BadCredentialsException {
        // TODO validate username and password length and/or special characters
        String username = req.username.toLowerCase().trim();
        try{
            userRepository.findByUsername(username).get();
            throw new BadCredentialsException();
        } catch (NoSuchElementException e) {
            MUser p = new MUser();
            p.username = 			username;
            p.password = 		    passwordEncoder.encode(req.password);
            userRepository.save(p);
        }
    }
}
