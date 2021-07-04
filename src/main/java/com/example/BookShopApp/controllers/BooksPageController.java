package com.example.BookShopApp.controllers;


import com.example.BookShopApp.data.dto.BooksPageDto;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/popular")
    public String booksPopularPage(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                   @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit,
                                   Model model) {
        model.addAttribute("popularBooks", bookService.getPopularBooks(offset, limit).getContent());
        return "books/popular";
    }

    @GetMapping("/author")
    public String booksAuthorPage(@RequestParam Integer authorId, @RequestParam String authorsFullName, Model model) {
        model.addAttribute("authorsFullName", authorsFullName);
        model.addAttribute("authorsBooks", bookService.getBooksByAuthorId(authorId));
        return "books/author";
    }

    @GetMapping("/recommended")
    @ResponseBody
    public BooksPageDto booksRecommendedPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/recent")
    public String getRecentPage(@RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate from,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate to,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                @RequestParam(value = "limit", required = false, defaultValue = "5") Integer limit, Model model) {
        model.addAttribute("recentBooks", bookService.getBooksByPubDate(from, to, offset, limit).getContent());
        return "books/recent";
    }
}
