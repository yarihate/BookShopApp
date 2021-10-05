package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.ResourceStorage;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/books")
public class BooksController {
    private BookRepository bookRepository;
    private final ResourceStorage storage;

    @Autowired
    public BooksController(BookRepository bookRepository, ResourceStorage storage) {
        this.bookRepository = bookRepository;
        this.storage = storage;
    }

    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        BookEntity bookEntity = bookRepository.findBookBySlug(slug);
        model.addAttribute("slugBook", bookEntity);
        return "books/slug";
    }

    @PostMapping("/{slug}/img/save")
    public String saveNewImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        BookEntity bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate);
        return ("redirect:/books/" + slug);
    }
}
