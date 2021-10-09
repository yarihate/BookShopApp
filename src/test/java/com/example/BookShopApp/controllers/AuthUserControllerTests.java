package com.example.BookShopApp.controllers;

import com.example.BookShopApp.security.RegistrationForm;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthUserControllerTests {
    private final MockMvc mockMvc;

    @Autowired
    AuthUserControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void login() throws Exception {
        mockMvc.perform(get("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new JSONObject().put("contact", "test@mail.ru").put("code", "1234567").toString()))
                .andExpect(status().isOk());
    }

    @Test
    void handleUserRegistration() throws Exception {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.com");
        registrationForm.setName("Tester");
        registrationForm.setPassword("1234567");
        registrationForm.setPhone("+79111111111");
        mockMvc.perform(post("/reg")
                .param("name", "1")
                .param("phone", "+7(911)1111111")
                .param("email", "t@mail.ru")
                .param("password", "11223344"))
                .andExpect(status().isOk());
    }
}
