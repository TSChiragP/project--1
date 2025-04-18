package com.sample.core.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

	@Autowired
	PostRepository postRepository;

	private final Logger logger = LoggerFactory.getLogger(PostService.class);

	@Autowired
	UserService userService;

	public List<Posts> getAllPosts() {
		logger.info("GETTING ALL POSTS");
		return postRepository.findAllByOrderByCreatedAtDesc();

	}

	public Posts addPost(Posts post) throws UserNotFoundException {
		logger.info("STARTING ADDING POST");
		String username = getUsernameFromPrincipal();
		if (Objects.nonNull(username)) {
			Users user = userService.getUser(username);
			post.setCreatedAt(LocalDateTime.now());
			post.setAuthor(user);
			Posts savedPost = postRepository.save(post);
			logger.info("POST SAVE POST WITH ID : " + savedPost.getId());
			return savedPost;
		} else {
			throw new UserNotFoundException("User not found with username : " + username);
		}
	}

	public void editPost(Integer postId, Posts post)
			throws PostNotFoundException, CustomAccessDeniedException, UserNotFoundException {
		log.info("EDITING A POST WITH ID : " + post.getId());

		Posts oldPost = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		log.info("OLD POST FOUND");
		String username = getUsernameFromPrincipal();
		if (Objects.nonNull(username)) {
			Users user = userService.getUser(username);
			if (Arrays.asList(user.getRoles().split(",")).contains("ADMIN")
					|| oldPost.getAuthor().getEmail().equals(username)) {
				oldPost.setTitle(post.getTitle());
				oldPost.setContent(post.getContent());
				postRepository.save(oldPost);
				log.info("POST EDITED AND SAVED");
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