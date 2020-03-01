package com.psych.game.auth;

import com.psych.game.exceptions.NoSuchUserException;
import com.psych.game.models.User;
import com.psych.game.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
This is a authentication service you build. This service needs to UserDetailsService interface which is implemented by
CustomUserDetails class
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty())
            throw new NoSuchUserException("No user registered with " + email);
        return new CustomUserDetails(user.get()); //Avoid confusion: here get() is part of Optional class that fetches user object
    }
}