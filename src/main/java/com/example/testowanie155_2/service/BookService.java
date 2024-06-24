package com.example.testowanie155_2.service;

import com.example.testowanie155_2.entity.Book;
import com.example.testowanie155_2.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
