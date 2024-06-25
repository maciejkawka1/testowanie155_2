package com.example.testowanie155_2.controller;

import com.example.testowanie155_2.entity.Book;
import com.example.testowanie155_2.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;

    @BeforeEach
    void setUp(){
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
        BDDMockito.given(bookService.save(any(Book.class))).willReturn(book);

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
        BDDMockito.given(bookService.findAll()).willReturn(books);

        // when
        // then
        mockMvc.perform(get("/api/books")).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }
    @Test
    public void givenBookId_whenGetBookById_thenReturnsBook() throws Exception {
        BDDMockito.given(bookService.findById(anyLong()))
                .willReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(book.getTitle())));
    }

    @Test
    public void givenBookId_whenGetBookById_thenReturnsNotFound() throws Exception {
        BDDMockito.given(bookService.findById(anyLong()))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUpdatedBook_whenUpdateBook_thenBookIsUpdated() throws Exception {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setIsbn("987654321");
        updatedBook.setPrice(new BigDecimal("20.99"));


        given(bookService.save(any(Book.class))).willReturn(updatedBook);

        mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedBook.getTitle())))
                .andExpect(jsonPath("$.author", is(updatedBook.getAuthor())));
    }
    @Test
    public void givenBookId_whenDeleteBook_thenBookIsDeleted() throws Exception {
        willDoNothing().given(bookService).delete(anyLong());

        mockMvc.perform(delete("/api/books/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void givenGenre_whenFindBooksByGenre_thenReturnsBooks() throws Exception {
        List<Book> books = Arrays.asList(new Book(), new Book());
        given(bookService.findBooksByGenre(anyString())).willReturn(books);

        mockMvc.perform(get("/api/books/genre/{genre}", "Fiction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }

    @Test
    public void givenAuthorAndYear_whenFindBooksByAuthorAndYear_thenReturnBooksList() throws Exception {
        // given
        String author = "J.R.R Tolkien";
        int year = 1954;
        List<Book> books = Collections.singletonList(book);
        given(bookService.findBooksByAuthorAndYear(author, year)).willReturn(books);

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
        String author = "J.R.R Tolkien";
        int year = 1954;
        given(bookService.findBooksByAuthorAndYear(author, year)).willReturn(new ArrayList<>());

        // when
        ResultActions response = mockMvc.perform(get("/api/books/author/{author}/year/{year}", author, year));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    public void givenYear_whenCountBooksByPublishedYear_thenReturnCount() throws Exception {
        // given
        int year = 1954;
        int count = 1;
        given(bookService.countBooksByPublishedYear(year)).willReturn(count);

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
        String isbn = "1234567890123";
        given(bookService.findByIsbn(isbn)).willReturn(Optional.of(book));

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
        given(bookService.findByIsbn(anyString())).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/api/books/isbn/{isbn}", "abc"));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    public void givenPrice_whenFindBooksCheaperThan_thenReturnBooksList() throws Exception {
        // given
        BigDecimal price = new BigDecimal("20.00");
        List<Book> books = Collections.singletonList(book);
        given(bookService.findBooksCheaperThan(price)).willReturn(books);

        // when
        ResultActions response = mockMvc.perform(get("/api/books/price/cheaper-than/{price}", price));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(books.size())));
    }

    @Test
    public void givenPrice_whenFindBooksCheaperThan_thenReturnNotFound() throws Exception {
        // given

        given(bookService.findBooksCheaperThan(null)).willReturn(new ArrayList<>());

        // when
        ResultActions response = mockMvc.perform(get("/api/books/price/cheaper-than/{price}", 20));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }
}