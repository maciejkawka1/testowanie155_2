package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Book;
import com.example.testowanie155_2.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // uruchamiamy serwer na
@AutoConfigureMockMvc
class BookControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        book = new Book();
        book.setId(1L);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setIsbn("test isbn");
        book.setPrice(new BigDecimal("19.99"));
        book.setGenre("Fiction");
        book.setPublishedYear(2021);
        book.setAvailable(true);
    }

    @Test
    public void givenBook_whenSave_thenReturnBook() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
        );

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.author", is(book.getAuthor())));
    }

    @Test
    public void givenListOfBooks_whenFindAll_thenReturnListOfBook() throws Exception {
        // given
        List<Book> books = Arrays.asList(book, new Book(), new Book());
        bookRepository.saveAll(books);

        // when
        // then
        mockMvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }

    @Test
    public void givenBookId_whenGetBookById_thenReturnsBook() throws Exception {
        Long id = bookRepository.save(book).getId();

        mockMvc.perform(get("/api/books/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void givenBookId_whenGetBookById_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUpdatedBook_whenUpdateBook_thenBookIsUpdated() throws Exception {
        Long id = bookRepository.save(book).getId();

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("987654321");
        updatedBook.setPrice(new BigDecimal("20.99"));


        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedBook.getTitle())))
                .andExpect(jsonPath("$.author", is(updatedBook.getAuthor())));
    }

    @Test
    public void givenBookId_whenDeleteBook_thenBookIsDeleted() throws Exception {
        Long id = bookRepository.save(book).getId();

        mockMvc.perform(delete("/api/books/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void givenGenre_whenFindBooksByGenre_thenReturnsBooks() throws Exception {
        List<Book> books = Arrays.asList(book, new Book(), new Book());
        bookRepository.saveAll(books);

        mockMvc.perform(get("/api/books/genre/{genre}", book.getGenre()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void givenAuthorAndYear_whenFindBooksByAuthorAndYear_thenReturnBooksList() throws Exception {
        // given
        String author = book.getAuthor();
        int year = book.getPublishedYear();
        List<Book> books = Collections.singletonList(book);
        bookRepository.saveAll(books);

        // when
        ResultActions response = mockMvc.perform(get("/api/books/author/{author}/year/{year}", author, year));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }

    @Test
    public void givenAuthorAndYear_whenFindBooksByAuthorAndYear_thenReturnNotFound() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/books/author/{author}/year/{year}", book.getAuthor(), book.getPublishedYear()));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenYear_whenCountBooksByPublishedYear_thenReturnCount() throws Exception {
        // given
        int year = bookRepository.save(book).getPublishedYear();
        int count = 1;

        // when
        ResultActions response = mockMvc.perform(get("/api/books/count/year/{year}", year));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(count)));
    }

    @Test
    public void givenValidIsbn_whenFindByIsbn_thenReturnBook() throws Exception {
        // given
        String isbn = bookRepository.save(book).getIsbn();

        // when
        ResultActions response = mockMvc.perform(get("/api/books/isbn/{isbn}", isbn));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn", is(book.getIsbn())));
    }

    @Test
    public void givenValidIsbn_whenFindByIsbn_thenReturnNotFound() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/books/isbn/{isbn}", book.getIsbn()));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenPrice_whenFindBooksCheaperThan_thenReturnBooksList() throws Exception {
        // given
        BigDecimal price = bookRepository.save(book).getPrice().add(new BigDecimal(1));

        // when
        ResultActions response = mockMvc.perform(get("/api/books/price/cheaper-than/{price}", price));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void givenPrice_whenFindBooksCheaperThan_thenReturnNotFound() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/books/price/cheaper-than/{price}", 20));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindAllAvailableBooks_thenReturnBooksList() throws Exception {
        // given
        List<Book> books = Arrays.asList(book, new Book(), new Book());
        bookRepository.saveAll(books);

        // when
        ResultActions response = mockMvc.perform(get("/api/books/available"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void whenFindAllAvailableBooks_thenReturnNotFound() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/books/available"));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


}