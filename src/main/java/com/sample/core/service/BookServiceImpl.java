package com.sample.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.core.domain.Book;
import com.sample.core.repository.BookRepository;

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
		Optional<Book> optionalBook = bookRepository.findById(id);

		return optionalBook.get();

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
