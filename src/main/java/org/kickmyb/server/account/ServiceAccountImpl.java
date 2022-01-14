package org.kickmyb.server.account;

import org.kickmyb.transfer.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
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


    // https://stackoverflow.com/questions/36498327/catch-dataintegrityviolationexception-in-transactional-service-method
    @Override
    @Transactional(rollbackFor = UsernameAlreadyTaken.class)
    public void signup(SignupRequest req) throws UsernameTooShort, PasswordTooShort, UsernameAlreadyTaken {
        String username = req.username.toLowerCase().trim();
        if (username.length() < 2) throw new UsernameTooShort();
        if (req.password.length() < 4) throw new PasswordTooShort();
        // validation de l'unicité est faite au niveau de la BD voir MUser.java
        try {
            MUser p = new MUser();
            p.username = username;
            p.password = passwordEncoder.encode(req.password);
            userRepository.saveAndFlush(p);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyTaken();
        }
    }
}
