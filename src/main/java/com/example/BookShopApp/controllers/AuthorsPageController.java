package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.Author;
import com.example.BookShopApp.data.AuthorService;
import com.example.BookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
public class AuthorsPageController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsPageController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsData();
    }

    @GetMapping()
    public String authorsPage() {
        return "/authors/index";
    }

    @GetMapping("/slug")
    public String authorsSlugPage(@RequestParam Integer authorId, @RequestParam String authorsFullName, Model model) {
        model.addAttribute("authorsFullName", authorsFullName);
        model.addAttribute("authorId", authorId);
        model.addAttribute("authorsBooks", bookService.getBooksByAuthorId(authorId));
        return "authors/slug";
    }
}
