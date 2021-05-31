package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.Author;
import com.example.BookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
public class AuthorsPageController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsData();
    }

    @GetMapping("/")
    public String authorsPage() {
        return "/authors/index";
    }

    @GetMapping("/slug")
    public String authorsSlugPage() {
        return "authors/slug";
    }
}
