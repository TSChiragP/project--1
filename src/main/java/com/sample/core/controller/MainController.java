package com.sample.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.core.domain.Book;
import com.sample.core.service.IBookService;

@RestController
public class MainController {

	@Autowired
	IBookService bookService;

	@PostMapping("/book")
	public ResponseEntity<Book> addBook(@RequestBody Book book) {
		return ResponseEntity.ok().body(bookService.addBook(book));
	}

	@GetMapping("/book")
	public ResponseEntity<List<Book>> getBooks() {
		return ResponseEntity.ok().body(bookService.getBooks());
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<Book> getBook(@PathVariable int id) {
		return ResponseEntity.ok().body(bookService.getBook(id));
	}

	@DeleteMapping("/book/{bookId}")
	public ResponseEntity<String> deleteBook(@PathVariable int id) {
		bookService.deleteBook(id);
		return ResponseEntity.ok().body("Book deleted successfully");
	}

	@PutMapping("/book")
	public ResponseEntity<Book> updateBook(@RequestBody Book book) {
		return ResponseEntity.ok().body(bookService.updateBook(book));
	}
}
