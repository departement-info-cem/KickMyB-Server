package org.kickmyb.server.account;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@Transactional
public class ServiceAccountImpl implements ServiceAccount {

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
    public void signup(SignupRequest req) {
        // TODO validate username and password length and/or special characters
        String username = req.username.toLowerCase().trim();
        // validation de l'unicité est faite au niveau de la BD voir MUser.java
        MUser p = new MUser();
        p.username = username;
        p.password = passwordEncoder.encode(req.password);
        userRepository.save(p);
    }
}
