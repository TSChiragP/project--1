package com.sample.core.controller;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sample.core.domain.Posts;
import com.sample.core.domain.Users;
import com.sample.core.exception.CustomAccessDeniedException;
import com.sample.core.exception.PostNotFoundException;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.repository.PostRepository;
import com.sample.core.service.PostService;
import com.sample.core.service.UserService;

@Controller
public class PostController {

	private final PostRepository postRepository;
	private final PostService postService;
	private final UserService userService;

	PostController(PostRepository postRepository, PostService postService, UserService userService) {
		this.postRepository = postRepository;
		this.postService = postService;
		this.userService = userService;
	}

	@GetMapping("/")
	private String getPosts(Model model, Principal principal) throws UserNotFoundException {
		if (Objects.nonNull(principal)) {
			Users user = userService.getUser(principal.getName());
			model.addAttribute("user", user);
		}
		model.addAttribute("posts", postService.getAllPosts());

		return "post_listing";
	}

	@GetMapping("/addPost")
	private String addPostsView() {
		return "add_post";
	}

	@GetMapping("/posts/{postId}")
	private String viewPosts(Model model, @PathVariable Integer postId, Principal principal)
			throws PostNotFoundException, UserNotFoundException {
		if (Objects.nonNull(principal)) {
			Users user = userService.getUser(principal.getName());
			model.addAttribute("user", user);
		}
		Posts postData = postService.getPostData(postId);

		model.addAttribute("post", postData);
		return "view_post";
	}

	@PostMapping("/addPost")
	private String addPosts(@ModelAttribute Posts post) throws UserNotFoundException {

		postService.addPost(post);
		return "redirect:/";
	}

	@GetMapping("/posts/delete/{postId}")
	private String deletePosts(@PathVariable Integer postId, Principal principal)
			throws PostNotFoundException, CustomAccessDeniedException, UserNotFoundException {
		postService.deletePost(postId);
		return "redirect:/";
	}

	@GetMapping("/posts/edit/{postId}")
	private String editPostsView(Model model, @PathVariable Integer postId) throws PostNotFoundException {
		Posts post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		model.addAttribute("post", post);
		return "edit_post";
	}

	@PostMapping("/posts/{postId}")
	private String editPost(@PathVariable Integer postId, @ModelAttribute Posts post)
			throws PostNotFoundException, CustomAccessDeniedException, UserNotFoundException {
		postService.editPost(postId, post);
		return "redirect:/";
	}

	@GetMapping("/myPost/{userId}")
	private String getMyPosts(@PathVariable Integer userId, Model model) {
		List<Posts> myPosts = postService.getMyPosts(userId);
		model.addAttribute("userPosts", myPosts);
		return "my_posts";
	}
}
