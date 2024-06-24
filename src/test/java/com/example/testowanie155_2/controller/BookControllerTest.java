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
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;


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
}