package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.services.BookService;
import com.example.BookShopApp.data.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/genres")
public class GenresPageController {
    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenresPageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping()
    public String genresPage(Model model) {
        model.addAttribute("genreList", genreService.getGenres());
        return "genres/index";
    }

    @GetMapping("/slug/{genreName}")
    public String genresSlugPage(@PathVariable(value = "genreName") String genreName,
                                 @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                 Model model) {
        model.addAttribute("books", bookService.receivePageOfBooksWithSpecificGenre(genreName, offset, limit).getContent());
        return "genres/slug";
    }
}
