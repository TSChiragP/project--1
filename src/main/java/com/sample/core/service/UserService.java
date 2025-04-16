package com.sample.core.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.core.domain.Users;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Users getUser(String username) throws UserNotFoundException {
		Users user = userRepository.findByEmail(username);
		if (Objects.nonNull(user)) {
			return user;
		}
		throw new UserNotFoundException("User not found with username : " + username);
	}

	public Users getUserById(Integer userId) throws UserNotFoundException {
		return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found !!"));
	}

	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}
}
