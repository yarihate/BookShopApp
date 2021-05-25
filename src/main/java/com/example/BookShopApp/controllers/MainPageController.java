package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.Book;
import com.example.BookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
//@RequestMapping("/bookshop")
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
}
