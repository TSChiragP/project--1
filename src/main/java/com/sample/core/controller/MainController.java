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
import org.springframework.web.client.RestTemplate;

import com.sample.core.domain.Book;
import com.sample.core.service.IBookService;

import jakarta.validation.Valid;

@RestController
public class MainController {

	@Autowired
	IBookService bookService;

	@Autowired
	RestTemplate restTemplate;

//	@GetMapping("/")
//	public ResponseEntity<Book> demo() {
//		String url = "http://localhost:8080/book";
//		Book requestDto = new Book();
//		requestDto.setName("scala");
//		requestDto.setPages(250);
//		requestDto.setAuthor("abcdefg");
//
//		return ResponseEntity.ok(restTemplate.postForObject(url, requestDto, Book.class));
//
//		// HttpEntity<Book> requestEntity = new HttpEntity<Book>(requestDto);
//		// return restTemplate.exchange(url, HttpMethod.POST, requestEntity,
//		// Book.class);
//	}

	@PostMapping("/book")
	public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
		return ResponseEntity.ok().body(bookService.addBook(book));
	}

	@GetMapping("/book")
	public ResponseEntity<List<Book>> getBooks() {
		return ResponseEntity.ok().body(bookService.getBooks());
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<Book> getBook(@PathVariable int bookId) {
		return ResponseEntity.ok().body(bookService.getBook(bookId));
	}

	@DeleteMapping("/book/{bookId}")
	public ResponseEntity<String> deleteBook(@PathVariable int bookId) {
		bookService.deleteBook(bookId);
		return ResponseEntity.ok().body("Book deleted successfully");
	}

	@PutMapping("/book")
	public ResponseEntity<Book> updateBook(@RequestBody Book book) {
		return ResponseEntity.ok().body(bookService.updateBook(book));
	}
}
