package com.sample.core.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTest {

	@Autowired
	MockMvc mockMvc;

	@AfterEach
	void resetSecurityContextHolder() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void publicHomeAccessWithoutLogin() throws Exception {
		mockMvc.perform(get("/home")).andExpect(status().isOk());
	}

	@Test
	void userHomeAccessWithoutLogin() throws Exception {
		mockMvc.perform(get("/user/home")).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "user@gmail.com", roles = "USER")
	void userHomeAccessWithLogin() throws Exception {
		mockMvc.perform(get("/user/home")).andExpect(status().isOk());
	}

	@Test
	void adminHomeAccessWithoutLogin() throws Exception {
		mockMvc.perform(get("/admin/home")).andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(username = "user@gmail.com", roles = "USER")
	void adminHomeNotAccessWithUserLogin() throws Exception {
		mockMvc.perform(get("/admin/home")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "admin@gmail.com", roles = "ADMIN")
	void adminHomeAccessWithAdminLogin() throws Exception {
		mockMvc.perform(get("/admin/home")).andExpect(status().isOk());
	}

	@Test
	void getLoginPageTest() throws Exception {

		mockMvc.perform(get("/login")).andExpect(status().isOk());
	}

}
