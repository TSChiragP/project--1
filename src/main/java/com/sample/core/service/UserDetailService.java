package com.sample.core.service;

import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sample.core.domain.Users;
import com.sample.core.repository.UserRepository;

@Component
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByEmail(username);
		if (Objects.nonNull(user)) {
//			new CustomUserDetails(user);
			return User.builder().username(user.getEmail()).password(user.getPassword())
					.roles(getRoles(user.getRoles())).build();

		}
		throw new UsernameNotFoundException("user " + username + " is not found!!");
	}

	private String[] getRoles(String roles) {
		if (Objects.nonNull(roles)) {
			return roles.split(",");
		} else {
			return new String[] { "USER" };
		}
	}

}
