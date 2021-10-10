package com.example.BookShopApp.security;

import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.services.BookstoreUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class BookstoreUserDetailsServiceTests {
    private final BookstoreUserDetailsService bookstoreUserDetailsService;

    @Autowired
    public BookstoreUserDetailsServiceTests(BookstoreUserDetailsService bookstoreUserDetailsService) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    }

    @Test
    void loadUserByUsername() {
        final String userName = "test2";
        UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsernameJwt(userName);
        assertNotNull(userDetails);
        Assertions.assertEquals(userName, userDetails.getUsername());
        Assertions.assertEquals(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                userDetails.getAuthorities());
    }

    @Test
    void processOAuthPostLogin() {
        final String email = "test@mail.ru";
        final String name = "test1";
        BookstoreUser bookStoreUser = bookstoreUserDetailsService.processOAuthPostLogin(email, name);
        assertNotNull(bookStoreUser);
        Assertions.assertEquals(name, bookStoreUser.getName());
        Assertions.assertEquals(email, bookStoreUser.getEmail());
    }
}
