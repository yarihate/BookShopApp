package com.example.BookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignPageController {

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
}
