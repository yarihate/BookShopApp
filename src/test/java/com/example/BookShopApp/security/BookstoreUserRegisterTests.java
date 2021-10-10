package com.example.BookShopApp.security;

import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import com.example.BookShopApp.data.services.BookstoreUserRegister;
import com.example.BookShopApp.security.RegistrationForm;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookStoreUserRegisterTests {

    private final BookstoreUserRegister bookStoreUserRegister;
    private final PasswordEncoder passwordEncoder;
    private RegistrationForm registrationForm;

    @MockBean
    private BookstoreUserRepository bookStoreUserRepositoryMock;

    @Autowired
    BookStoreUserRegisterTests(BookstoreUserRegister bookStoreUserRegister, PasswordEncoder passwordEncoder) {
        this.bookStoreUserRegister = bookStoreUserRegister;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.com");
        registrationForm.setName("Tester");
        registrationForm.setPassword("1234567");
        registrationForm.setPhone("+79111111111");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewUser() {
        BookstoreUser returnedBookStoreUser = new BookstoreUser();
        returnedBookStoreUser.setName(registrationForm.getName());
        returnedBookStoreUser.setEmail(registrationForm.getEmail());
        returnedBookStoreUser.setPhone(registrationForm.getPhone());
        returnedBookStoreUser.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

        Mockito.doReturn(returnedBookStoreUser).when(bookStoreUserRepositoryMock).save(Mockito.any(BookstoreUser.class));

        BookstoreUser newUser = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNotNull(newUser);
        assertTrue(CoreMatchers.is(newUser.getPhone()).matches(registrationForm.getPhone()));
        assertTrue(CoreMatchers.is(newUser.getEmail()).matches(registrationForm.getEmail()));
        assertTrue(CoreMatchers.is(newUser.getName()).matches(registrationForm.getName()));
        assertTrue(passwordEncoder.matches(registrationForm.getPassword(), newUser.getPassword()));

        Mockito.verify(bookStoreUserRepositoryMock, Mockito.times(1)).save(Mockito.any(BookstoreUser.class));
    }

    @Test
    void registerNewUserFail() {
        Mockito.doReturn(new BookstoreUser()).when(bookStoreUserRepositoryMock).findBookstoreUserByEmail(registrationForm.getName());
        BookstoreUser newUser = bookStoreUserRegister.registerNewUser(registrationForm);
        assertNull(newUser);
    }
}