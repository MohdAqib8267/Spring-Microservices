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
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public User register(User user) {
		//1st we need to encode our password
		user.setPassword(encoder.encode(user.getPassword()));
		//now store data into db
		userRepository.save(user);
		return user;
	}
	
	public String verify(User user) {
		//1st authenticate user using spring security
		// if it is authenticated then generate token. (These are two different tasks)
		
		
		//Authentication Manage authenticate user
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		if(authentication.isAuthenticated()) {
			return jwtService.generateToken(user.getUsername(), user.getRole());
		}
		else {
			return "Something went wrong";
		}
		
	}
}
