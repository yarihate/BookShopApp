package com.example.BookShopApp.security;

import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserRepositoryTests {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserRepositoryTests(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Test
    public void testAddNewUser() {
        BookstoreUser bookStoreUser = new BookstoreUser();
        bookStoreUser.setEmail("testt@mail.com");
        bookStoreUser.setName("ttest");
        bookStoreUser.setPassword("12345678");
        bookStoreUser.setPhone("+79111111111");
        assertNotNull(bookstoreUserRepository.save(bookStoreUser));
    }
}