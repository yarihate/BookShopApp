package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.author.AuthorEntity;
import com.example.BookShopApp.data.services.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
@Api("authors data")
public class AuthorsPageController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsPageController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("authorsMap")
    public Map<String, List<AuthorEntity>> authorsMap() {
        return authorService.getAuthorsData();
    }

    @GetMapping()
    @ApiOperation("method to get map of authors")
    public String authorsPage() {
        return "/authors/index";
    }

    @GetMapping("/slug")
    public String authorsSlugPage(@RequestParam Integer authorId, @RequestParam String authorsFullName, Model model) {
        model.addAttribute("author", authorService.getAuthorById(authorId));
        return "authors/slug";
    }

    @GetMapping("/test")
    @ResponseBody
    public Map<String, List<AuthorEntity>> test() {
        return authorService.getAuthorsData();
    }
}
