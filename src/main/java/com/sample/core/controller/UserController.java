package com.sample.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sample.core.domain.Users;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("/profile/{userId}")
	public String viewProfile(Model model, @PathVariable Integer userId) throws UserNotFoundException {

		Users user = userService.getUserById(userId);

		model.addAttribute("user", user);
		return "view_profile";
	}
}
