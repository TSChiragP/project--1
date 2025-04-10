package com.sample.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.core.domain.Users;
import com.sample.core.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
public class RegistrationController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/register/user")
	public Users registerUser(@Valid @RequestBody Users user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
