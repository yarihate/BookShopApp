package com.example.BookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenresPageController {

    @GetMapping("/")
    public String genresPage() {
        return "genres/index";
    }

    @GetMapping("/slug")
    public String genresSlugPage() {
        return "genres/slug";
    }
}
