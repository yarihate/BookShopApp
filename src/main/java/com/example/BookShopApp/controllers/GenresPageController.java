package com.example.BookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bookshop")
public class GenresPageController {

    @GetMapping("/genres/index")
    public String genresPage() {
        return "genres/index";
    }
}
