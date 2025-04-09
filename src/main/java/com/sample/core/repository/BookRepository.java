package com.sample.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.core.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}
