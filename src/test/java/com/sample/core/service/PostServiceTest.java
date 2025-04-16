package com.sample.core.service;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.core.domain.Posts;
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

	@Test
	void addPostTest() throws UserNotFoundException {
		Posts post = new Posts();
		post.setId(1);
		post.setTitle("test post added");
		post.setContent("test post content added");

		Mockito.when(postRepository.save(post)).thenReturn(post);
		Posts addedPost = postService.addPost(post);
		Assertions.assertEquals(post.getTitle(), addedPost.getTitle());
		Assertions.assertTrue(addedPost.getId() == 1);
	}

	@BeforeAll
	static void beforeAllTest() {
		System.out.println("before All");
	}

	@BeforeEach
	void beforeEach() {
		System.out.println("before each");
	}

	@AfterEach
	void afterEach() {
		System.out.println("after each");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("after all");
	}

	@Test
	void deletePostTest() throws CustomAccessDeniedException, PostNotFoundException {
		Posts post = new Posts();
		post.setId(1);
		Mockito.doNothing().when(postRepository).deleteById(1);
		Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
		postService.deletePost(1);
		Mockito.verify(postRepository, times(1)).deleteById(1);

	}
}
