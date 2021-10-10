package com.example.BookShopApp;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookShopAppApplicationTests {
    private final BookShopAppApplication application;

    @Autowired
    public BookShopAppApplicationTests(BookShopAppApplication application) {
        this.application = application;
    }

    @Value("${auth.secret}")
    String authSecret;


    @Test
    void verify() {
        assertThat(authSecret, Matchers.containsString("box"));
    }

    @Test
    void contextLoads() {
        assertNotNull(application);
    }
}
