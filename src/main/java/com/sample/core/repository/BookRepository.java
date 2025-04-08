package com.sample.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sample.core.domain.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
