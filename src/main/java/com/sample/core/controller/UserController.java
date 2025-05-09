package com.sample.core.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sample.core.domain.Users;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.service.UserService;

@Controller
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/profile/{userId}")
	public String viewProfile(Model model, @PathVariable Integer userId) throws UserNotFoundException {

		Users user = userService.getUserById(userId);

		model.addAttribute("user", user);
		return "view_profile";
	}

	@GetMapping("/users")
	public String getAllUsers(Model model) {

		List<Users> users = userService.getAllUsers();

		model.addAttribute("users", users);
		return "all_user";
	}
}
