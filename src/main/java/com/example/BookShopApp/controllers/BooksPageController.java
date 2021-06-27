package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BooksPageController {

    private final BookService bookService;

    @Autowired
    public BooksPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/slug")
    public String booksSlugPage() {
        return "books/slug";
    }

    @GetMapping("/recent")
    public String booksRecentPage() {
        return "books/recent";
    }

    @GetMapping("/popular")
    public String booksPopularPage() {
        return "books/popular";
    }

    @GetMapping("/author")
    public String booksAuthorPage(@RequestParam Integer authorId, @RequestParam String authorsFullName, Model model) {
        model.addAttribute("authorsFullName", authorsFullName);
        model.addAttribute("authorsBooks", bookService.getBooksByAuthorId(authorId));
        return "books/author";
    }
}
