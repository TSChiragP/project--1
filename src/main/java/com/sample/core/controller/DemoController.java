package com.sample.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

	@GetMapping("/user/home")
	public String userEndpoint() {
		return "user_home";
	}

	@GetMapping("/home")
	public String publicEndpoint() {
		return "user_home";
	}

	@GetMapping("/admin/home")
	public String adminEndpoint() {
		return "admin_home";
	}

	@GetMapping("/login")
	public String loginEndpoint() {
		return "custom_login";
	}
}
