package com.sample.core.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public void addPost(Posts post, String username) throws UserNotFoundException {

		Users user = userService.getUser(username);
		post.setCreatedAt(LocalDateTime.now());
		post.setAuthor(user);
		postRepository.save(post);
	}

	public void editPost(Integer postId, Posts post) throws PostNotFoundException {
		Posts oldPost = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		oldPost.setTitle(post.getTitle());
		oldPost.setContent(post.getContent());
		postRepository.save(oldPost);
	}

	public void deletePost(Integer postId, Principal principal)
			throws PostNotFoundException, CustomAccessDeniedException {
		Posts post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found !!"));
		if (post.getAuthor().getEmail().equals(principal.getName())) {
			postRepository.deleteById(postId);
		} else {
			throw new CustomAccessDeniedException("You dont have access to delete this post");
		}
	}

	public Posts getPostData(Integer postId) throws PostNotFoundException {
		return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found !!"));

	}

	public List<Posts> getMyPosts(Integer id) {
		return postRepository.findByUserId(id);
	}
}
