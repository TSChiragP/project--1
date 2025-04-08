package com.sample.core.service;

import java.util.List;

import com.sample.core.domain.Book;

public interface IBookService {

	Book addBook(Book book);

	List<Book> getBooks();

	Book getBook(int Id);

	void deleteBook(int Id);

	Book updateBook(Book book);
}
