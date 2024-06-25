package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Book;
import com.example.testowanie155_2.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book){
        return ResponseEntity.ok(bookService.save(book));
    }
    @GetMapping
    public ResponseEntity<List<Book>> findAll(){
        return ResponseEntity.ok(bookService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.save(book));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> findBooksByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(bookService.findBooksByGenre(genre));
    }

    @GetMapping("/author/{author}/year/{year}")
    public ResponseEntity<List<Book>> findBooksByAuthorAndYear(@PathVariable String author, @PathVariable int year) {
        return ResponseEntity.ok(bookService.findBooksByAuthorAndYear(author, year));
    }

}
