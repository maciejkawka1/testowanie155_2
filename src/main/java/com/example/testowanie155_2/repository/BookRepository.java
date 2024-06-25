package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(String genre);

    List<Book> findByAuthorAndPublishedYear(String author, int publishedYear);

    List<Book> findBooksByAuthorAndPublishedYear(String author, int year);

    int countBooksByPublishedYear(int year);

    Optional<Book> findByIsbn(String isbn);
}
