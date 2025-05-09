package com.sample.core.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
//		Boolean isAdmin = authentication.getAuthorities().stream()
//				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

//		if (isAdmin) {
//		setDefaultTargetUrl("/post");
//		} else {
//			setDefaultTargetUrl("/user/home");
//		}

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
