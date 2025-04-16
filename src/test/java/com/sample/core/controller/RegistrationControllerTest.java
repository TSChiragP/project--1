package com.sample.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.core.domain.Users;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void registerUserSuccessfulTest() throws Exception {
		Users user = new Users();
		user.setUserName("new user1");
		user.setEmail("new@gmail.com");
		user.setPassword("123");
		user.setRoles("USER,ADMIN");

		mockMvc.perform(post("/register/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.userName").value("new user1"))
				.andExpect(jsonPath("$.password").value(Matchers.not("123")))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.roles").exists());
	}

}
