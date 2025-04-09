package com.sample.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.core.domain.Book;
import com.sample.core.exception.BookNotFoundException;
import com.sample.core.repository.BookRepository;

@Service
public class BookServiceImpl implements IBookService {

	@Autowired
	BookRepository bookRepository;

	@Override
	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book getBook(int id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("book not found with id : " + id));
		return book;

	}

	@Override
	public void deleteBook(int id) {
		bookRepository.deleteById(id);
	}

	@Override
	public Book updateBook(Book book) {
		return bookRepository.save(book);
	}

}
