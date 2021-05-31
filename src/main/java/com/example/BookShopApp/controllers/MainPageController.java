package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.Book;
import com.example.BookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getBooksData();
    }

    @GetMapping("/")
    public String mainPage() {
//        model.addAttribute("bookData", bookService.getBooksData());
//        model.addAttribute("searchPlaceholder", "new search placeholder");
//        model.addAttribute("serverTime", new Date());
//        model.addAttribute("placeholderTextPart2", "SERVER");
//        model.addAttribute("messageTemplate", "searchbar.placeholder2");
        return "index";
    }

    @GetMapping("/postponed")
    public String postponedPage() {
        return "postponed";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/signin")
    public String signinPage() {
        return "signin";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents/index";
    }
}
