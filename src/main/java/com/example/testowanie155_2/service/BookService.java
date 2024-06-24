package com.example.testowanie155_2.service;

import com.example.testowanie155_2.entity.Book;
import com.example.testowanie155_2.repository.BookRepository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public void delete(long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }
}
