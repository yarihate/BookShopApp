package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.BookEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookRepositoryTests {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBooksByAuthor_FirstName() {
        String slug = "book-ocp-206";
        BookEntity result = bookRepository.findBookBySlug(slug);
        assertEquals(slug, result.getSlug());
    }

    @Test
    void findBooksByTitleContaining() {
        final String query = "W";
        List<BookEntity> bookListByTitleContaining = bookRepository.findBooksByTitleContaining(query);
        assertNotNull(bookListByTitleContaining);
        assertFalse(bookListByTitleContaining.isEmpty());

        for (BookEntity book : bookListByTitleContaining) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertTrue(book.getTitle().contains(query));
        }
    }

    @Test
    void getBestsellers() {
        List<BookEntity> bestsellerBooks = bookRepository.getBestsellers();
        assertNotNull(bestsellerBooks);
        assertFalse(bestsellerBooks.isEmpty());
        assertThat(bestsellerBooks.size()).isGreaterThan(1);
    }
}