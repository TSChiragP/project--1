package com.sample.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sample.core.service.UserDetailService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

	@Autowired
	UserDetailService userDetailService;

	private static String[] NON_AUTHENTICATED_URLS = { "/", "/register/user" };
	private static String[] ADMIN_URLS = { "/admin/**" };
	private static String[] USER_URLS = { "/user/**" };

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		return httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(request -> {
			request.requestMatchers(NON_AUTHENTICATED_URLS).permitAll();
			request.requestMatchers(ADMIN_URLS).hasRole("ADMIN");
			request.requestMatchers(USER_URLS).hasAnyRole("USER", "ADMIN");
			request.anyRequest().authenticated();
		}).formLogin(formLogin -> formLogin.loginPage("/login").successHandler(new AuthenticationSuccessHandler())
				.permitAll()).build();
	}

	@Bean
	UserDetailsService getUserDetailsService() {
		return userDetailService;
	}

	@Bean
	AuthenticationProvider getAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return authenticationProvider;
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
