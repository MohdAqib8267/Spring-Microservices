package com.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.modal.User;
import com.security.repository.UserRepository;

@Service
public class UserService {

	@Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;
 
    @Autowired
    private UserRepository repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }
}
