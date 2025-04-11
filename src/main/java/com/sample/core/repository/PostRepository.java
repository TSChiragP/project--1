package com.sample.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.core.domain.Posts;
import com.sample.core.domain.Users;

@Repository
public interface PostRepository extends JpaRepository<Posts, Integer> {

	List<Posts> findAllByOrderByCreatedAtDesc();

	@Query(value = "SELECT * FROM posts p WHERE p.user_id = :userId", nativeQuery = true)
	List<Posts> findByUserId(Integer userId);

}
