package com.sample.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sample.core.domain.Book;
import com.sample.core.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

	@Mock
	BookRepository bookRepository;

	@InjectMocks
	BookServiceImpl bookService;

	@Test
	void addBookTest() {
		Book dummyBook = this.getDummyBook(1, "Dummy book", 420, "Dummy author");
		when(bookRepository.save(dummyBook)).thenReturn(dummyBook);
		bookService.addBook(dummyBook);
		System.out.println(dummyBook.getId());
		assertNotNull(dummyBook.getId());
	}

	@Test
	void getAllBooksTest() {

		Book book1 = this.getDummyBook(1, "book1", 12, "author-1");
		Book book2 = this.getDummyBook(2, "book2", 23, "author-2");

		List<Book> books = List.of(book1, book2);
		when(bookRepository.findAll()).thenReturn(books);
		bookService.getBooks();
		assertEquals(2, books.size());

	}

	@Test
	void getBookTest() {
		Book book = this.getDummyBook(1, "book1", 12, "author-1");

		when(bookRepository.findById(1)).thenReturn(Optional.of(book));
		bookService.getBook(1);
		assertNotNull(book);
		assertEquals(1, book.getId());
	}

	@Test
	void deleteBookTest() {
		bookService.deleteBook(1);
		verify(bookRepository, times(1)).deleteById(1);
	}

	@Test
	void updateBookTest() {

		Book book = this.getDummyBook(1, "book1", 12, "author-1");
		Book updatedBook = this.getDummyBook(1, "updatedBook", 23, "author-1");
		when(bookRepository.save(book)).thenReturn(updatedBook);
		bookService.updateBook(book);
		assertEquals(book.getId(), updatedBook.getId());
		assertEquals("updatedBook", updatedBook.getName());
	}

	Book getDummyBook(Integer id, String name, int pages, String author) {
		Book book = new Book();
		book.setId(id);
		book.setName(name);
		book.setPages(pages);
		book.setAuthor(author);
		return book;

	}

}
