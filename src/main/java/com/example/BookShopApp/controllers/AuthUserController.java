package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.SmsCode;
import com.example.BookShopApp.data.services.BookstoreUserRegister;
import com.example.BookShopApp.data.services.SmsService;
import com.example.BookShopApp.security.ContactConfirmationPayload;
import com.example.BookShopApp.security.ContactConfirmationResponse;
import com.example.BookShopApp.security.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController {
    private final BookstoreUserRegister bookstoreUserRegister;
    private final SmsService smsService;
    private final JavaMailSender javaMailSender;

    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @Autowired
    public AuthUserController(BookstoreUserRegister bookstoreUserRegister, SmsService smsService, JavaMailSender javaMailSender) {
        this.bookstoreUserRegister = bookstoreUserRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/signin")
    public String handleSingIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSingUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/requestEmailConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstore00@mail.ru");
        message.setTo(payload.getContact());
        SmsCode smsCode = new SmsCode(smsService.generateCode(), 300);
        smsService.saveNewCode(smsCode);
        message.setSubject("Bookstore mail verification");
        message.setText("Verification code id " + smsCode.getCode());
        javaMailSender.send(message);
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        bookstoreUserRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload, HttpServletResponse response) {
        ContactConfirmationResponse loginResponse = bookstoreUserRegister.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        response.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(Model model) {
        model.addAttribute("curUser", bookstoreUserRegister.getCurrentUser());
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUser", bookstoreUserRegister.getCurrentUser());
        return "profile";
    }

    @GetMapping("/403")
    public String handle403() {
        return "403";
    }

    @GetMapping("/401")
    public String handle401() {
        return "401";
    }

}
