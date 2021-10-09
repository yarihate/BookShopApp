package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.BookstoreUser;
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
        bookStoreUser.setEmail("test3@mail.com");
        bookStoreUser.setName("John");
        bookStoreUser.setPassword("1234567");
        bookStoreUser.setPhone("+79111111111");
        bookStoreUser.setId(2);

        assertNotNull(bookstoreUserRepository.save(bookStoreUser));
    }
}