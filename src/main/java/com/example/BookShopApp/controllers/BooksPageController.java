package com.example.BookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BooksPageController {
    @GetMapping("/recent")
    public String booksRecentPage() {
        return "books/recent";
    }

    @GetMapping("/popular")
    public String booksPopularPage() {
        return "books/popular";
    }

    @GetMapping("/author")
    public String booksAuthorPage() {
        return "books/author";
    }
}
