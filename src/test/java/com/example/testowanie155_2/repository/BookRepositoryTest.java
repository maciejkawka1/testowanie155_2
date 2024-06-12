package com.example.testowanie155_2.repository;

import com.example.testowanie155_2.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book(
                1L,
                "Władca pierścieni",
                "J.R.R Tolkien",
                "123456",
                1954,
                "Fantasy",
                new BigDecimal("49.99"),
                true);
    }

    @Test
    @DisplayName("Test zapisywania ksiazki")
    public void testSaveBook() {
        // given
        // when
        Book saved = bookRepository.save(book);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }


    // test szukania wszystkich ksiązek
    @Test
    @DisplayName("Test szukania wszystkich ksiązek")
    public void testFindAllBooks() {
        // given
        Book book2 = new Book(
                2L,
                "Pan Tadeusz",
                "Adam Mickiesicz",
                "123456",
                1954,
                "Fantasy",
                new BigDecimal("49.99"),
                true);
        bookRepository.save(book);
        bookRepository.save(book2);
        // when
        List<Book> books = bookRepository.findAll();
        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(2);
    }
// test uaktualnainia ksiązki
    @Test
    @DisplayName("Test uaktualnainia ksiązki")
    public void testUpdateBook() {
        // given
        bookRepository.save(book);
        book.setAuthor("Jarosław Wielki i Skromny :)");
        // when
        Book saved = bookRepository.save(book);
        // then
        assertThat(saved.getAuthor()).isEqualTo("Jarosław Wielki i Skromny :)");
    }

    // test usuwania ksiazki
    @Test
    @DisplayName("Test usuwania ksiązki")
    public void testDeleteBook() {
        // given
        Long id = bookRepository.save(book).getId();

        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findById(id)).isNotPresent();
    }
    // test wyszukiwania po gatunku
    @Test
    @DisplayName("Test wyszukiwania po gatunku")
    public void testFindByGenre() {
        // given
        Book book2 = new Book(
                2L,
                "Pan Tadeusz",
                "Adam Mickiesicz",
                "123456",
                1954,
                "Fantasy",
                new BigDecimal("49.99"),
                true);
        String genre = bookRepository.save(book).getGenre();
        bookRepository.save(book2);

        // when
        List<Book> books = bookRepository.findByGenre(genre);

        // then
        assertThat(books).allMatch(book -> book.getGenre().equals(genre));
    }

// test wyszukiwania po autorze i dacie publikacji - pozytywny scenariusz
@Test
@DisplayName("Test wyszukiwania po autorze i dacie publikacji - pozytywny scenariusz")
public void testFindByAuthorAndPublishedYear() {
    // given
    Book book2 = new Book(
            2L,
            "Pan Tadeusz",
            "Adam Mickiesicz",
            "123456",
            1954,
            "Fantasy",
            new BigDecimal("49.99"),
            true);
    bookRepository.save(book);
    bookRepository.save(book2);

    // when
    List<Book> books = bookRepository.findByAuthorAndPublishedYear("Adam Mickiesicz", 1954);

    // then
    assertThat(books).allMatch(book -> book.getAuthor().equals("Adam Mickiesicz") && book.getPublishedYear()==1954);
}
// test wyszukiwania po autorze i dacie publikacji - negatywny scenariusz

    @Test
    @DisplayName("Test wyszukiwania po autorze i dacie publikacji - negatywny scenariusz")
    public void testFindByAuthorAndPublishedYearNegativeScenario() {
        // given
        // when
        List<Book> books = bookRepository.findByAuthorAndPublishedYear("Adam Mickiesicz", 1954);

        // then
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(0);
    }
}