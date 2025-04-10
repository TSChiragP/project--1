package com.sample.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.core.domain.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

	Users findByEmail(String email);
}
