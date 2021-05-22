package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/bookshop")
public class MainPageController {

    private final BookService bookService;

    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        model.addAttribute("bookData", bookService.getBooksData());
        model.addAttribute("searchPlaceholder", "new search placeholder");
        model.addAttribute("serverTime", new Date());
        model.addAttribute("placeholderTextPart2", "SERVER");
        model.addAttribute("messageTemplate", "searchbar.placeholder2");
        return "index";
    }
}
