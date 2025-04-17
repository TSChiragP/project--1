package com.sample.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.sample.core.domain.Posts;
import com.sample.core.domain.Users;
import com.sample.core.exception.CustomAccessDeniedException;
import com.sample.core.exception.PostNotFoundException;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	PostRepository postRepository;

	@InjectMocks
	PostService postService;

	@Mock
	UserService userService;

	@BeforeEach
	void beforeEach() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void getAllPostsTest() {

		Posts dummyPost1 = getDummyPost(1, "title-1", "content-1", "user1@gmail.com");
		Posts dummyPost2 = getDummyPost(2, "title-2", "content-2", "user2@gmail.com");

		List<Posts> posts = List.of(dummyPost1, dummyPost2);

		when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(posts);

		List<Posts> allPosts = postService.getAllPosts();

		assertEquals(posts.size(), allPosts.size());
		assertEquals(dummyPost1.getId(), allPosts.get(0).getId());
	}

	@Test
	void addPostSuccessfullTest() throws UserNotFoundException {

		// creating a mock user
		String username = "mockUser@gmail.com";

		Users mockuser = this.getDummyUser(1, username, "ADMIN");

		this.mockSecurityContextHolder(username, "ROLE_ADMIN");

		// creating a post
		Posts post = getDummyPost(1, "test post added", "test post content added", username);

		when(userService.getUser(username)).thenReturn(mockuser);
		when(postRepository.save(post)).thenReturn(post);

		Posts addedPost = postService.addPost(post);
		assertNotNull(addedPost.getCreatedAt());
		assertEquals(post.getTitle(), addedPost.getTitle());
		assertTrue(addedPost.getId() == 1);
	}

	@Test
	void addPostShouldThrowUserNotFoundExceptionTest() {
		// creating a post
		Posts post = getDummyPost(1, "test post added", "test post content added", "mockUser@gmail.com");

		UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
			postService.addPost(post);
		});

		assertEquals("User not found with username : null", ex.getMessage());

	}

	@Test
	void deletePostSuccessfulByAdminTest()
			throws CustomAccessDeniedException, PostNotFoundException, UserNotFoundException {
		String username = "mockUser@gmail.com";

		Users mockuser = this.getDummyUser(1, username, "ADMIN");

		this.mockSecurityContextHolder(username, "ROLE_ADMIN");

		Posts post = new Posts();
		post.setId(1);
		when(postRepository.findById(1)).thenReturn(Optional.of(post));
		doNothing().when(postRepository).deleteById(1);
		when(userService.getUser(username)).thenReturn(mockuser);
		postService.deletePost(1);
		verify(postRepository, times(1)).deleteById(1);

	}

	@Test
	void deletePostSuccessfulByNormalUser()
			throws CustomAccessDeniedException, PostNotFoundException, UserNotFoundException {
		String username = "mockUser@gmail.com";
		Users user = this.getDummyUser(1, username, "USER");

		// creating a post
		Posts post = getDummyPost(1, "test post added", "test post content added", username);

		this.mockSecurityContextHolder(username, "ROLE_USER");
		doNothing().when(postRepository).deleteById(1);
		when(postRepository.findById(1)).thenReturn(Optional.of(post));
		when(userService.getUser(username)).thenReturn(user);
		postService.deletePost(1);
		verify(postRepository, times(1)).deleteById(1);
	}

	@Test
	void deletePostWithUserRoleShouldFailTest() throws UserNotFoundException {

		String authorUsername = "mockUser@gmail.com";
		String username2 = "loginUser@gmail.com";

		// creating a post
		Posts post = getDummyPost(1, "test post added", "test post content added", authorUsername);

		Users mockuser = this.getDummyUser(1, username2, "USER");

		this.mockSecurityContextHolder(username2, "ROLE_USER");
		when(postRepository.findById(1)).thenReturn(Optional.of(post));
		when(userService.getUser(username2)).thenReturn(mockuser);
		assertThrows(CustomAccessDeniedException.class, () -> {
			postService.deletePost(1);
		});

	}

	@Test
	void editPostSuccessfulTest() throws CustomAccessDeniedException, PostNotFoundException, UserNotFoundException {
		String authorUsername = "mockUser@gmail.com";
		Posts dummyPost = getDummyPost(1, "test post added", "test post content added", authorUsername);
		Posts updatedPost = getDummyPost(1, "test post updated", "test post content updated", authorUsername);
		Users dummyUser = getDummyUser(1, authorUsername, "USER");

		when(postRepository.findById(1)).thenReturn(Optional.of(dummyPost));
		when(userService.getUser(authorUsername)).thenReturn(dummyUser);
		this.mockSecurityContextHolder(authorUsername, "ROLE_USER");
		when(postRepository.save(dummyPost)).thenReturn(updatedPost);
		postService.editPost(1, updatedPost);

		assertEquals(updatedPost.getId(), dummyPost.getId());

	}

	@Test
	void editPostShouldThrowAccessDenieExceptionTest()
			throws CustomAccessDeniedException, PostNotFoundException, UserNotFoundException {
		String authorUsername = "mockUser@gmail.com";
		Posts dummyPost = getDummyPost(1, "test post added", "test post content added", authorUsername);
		Posts updatedPost = getDummyPost(1, "test post updated", "test post content updated", authorUsername);

		Users dummyUser = getDummyUser(1, "test@gmail.com", "USER");
		when(userService.getUser("test@gmail.com")).thenReturn(dummyUser);
		this.mockSecurityContextHolder("test@gmail.com", "ROLE_USER");
		when(postRepository.findById(1)).thenReturn(Optional.of(dummyPost));
		assertThrows(CustomAccessDeniedException.class, () -> {
			postService.editPost(1, updatedPost);
		});
	}

	void mockSecurityContextHolder(String username, String role) {

		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);

		UserDetails user = new User(username, "password", List.of(simpleGrantedAuthority));
		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	Users getDummyUser(Integer id, String email, String role) {
		Users user = new Users();

		user.setId(id);
		user.setEmail(email);
		user.setRoles(role);

		return user;
	}

	Posts getDummyPost(Integer id, String title, String cotent, String username) {
		Users user = new Users();
		user.setUserName(username);
		user.setEmail(username);

		Posts post = new Posts();
		post.setId(id);
		post.setTitle(title);
		post.setContent(cotent);
		post.setAuthor(user);
		return post;
	}

}
