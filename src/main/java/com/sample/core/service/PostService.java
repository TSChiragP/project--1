package com.sample.core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sample.core.domain.Posts;
import com.sample.core.domain.Users;
import com.sample.core.exception.CustomAccessDeniedException;
import com.sample.core.exception.PostNotFoundException;
import com.sample.core.exception.UserNotFoundException;
import com.sample.core.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserService userService;

	public List<Posts> getAllPosts() {
		return postRepository.findAllByOrderByCreatedAtDesc();

	}

	public Posts addPost(Posts post) throws UserNotFoundException {
		String username = getUsernameFromPrincipal();
		if (Objects.nonNull(username)) {
			Users user = userService.getUser(username);
			post.setCreatedAt(LocalDateTime.now());
			post.setAuthor(user);
			return postRepository.save(post);
		} else {
			throw new UserNotFoundException("User not found with username : " + username);
		}
	}

	public void editPost(Integer postId, Posts post)
			throws PostNotFoundException, CustomAccessDeniedException, UserNotFoundException {

		Posts oldPost = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		String username = getUsernameFromPrincipal();
		if (Objects.nonNull(username)) {
			Users user = userService.getUser(username);
			if (Arrays.asList(user.getRoles().split(",")).contains("ADMIN")
					|| oldPost.getAuthor().getEmail().equals(username)) {
				oldPost.setTitle(post.getTitle());
				oldPost.setContent(post.getContent());
				postRepository.save(oldPost);
			} else {
				throw new CustomAccessDeniedException("You dont have access to edit this post");
			}
		}

	}

	public void deletePost(Integer postId)
			throws PostNotFoundException, CustomAccessDeniedException, UserNotFoundException {
		Posts post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		String username = this.getUsernameFromPrincipal();
		if (Objects.nonNull(username)) {
			Users user = userService.getUser(username);
			if (Arrays.asList(user.getRoles().split(",")).contains("ADMIN")
					|| post.getAuthor().getEmail().equals(username)) {
				postRepository.deleteById(postId);
			} else {
				throw new CustomAccessDeniedException("You dont have access to delete this post");
			}
		}
	}

	public Posts getPostData(Integer postId) throws PostNotFoundException {
		return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found !!"));

	}

	public List<Posts> getMyPosts(Integer id) {
		return postRepository.findByUserId(id);
	}

	private String getUsernameFromPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(authentication)) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails principalUserDetails = (UserDetails) principal;
				return principalUserDetails.getUsername();
			}
		}
		return null;

	}
}