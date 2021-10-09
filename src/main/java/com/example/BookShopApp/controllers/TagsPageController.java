package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.dto.TagDto;
import com.example.BookShopApp.data.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/tags")
public class TagsPageController {

    private final BookService bookService;

    @Autowired
    public TagsPageController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @GetMapping()
    public String tagsPage(@RequestParam int id,
                           @RequestParam String name,
                           @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                           @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                           Model model) {
        model.addAttribute("booksByTag", bookService.getBooksByTag((long) id, offset, limit).getContent());
        TagDto tagDto = new TagDto(id, name, null);
        model.addAttribute("tag", tagDto);
        return "tags/index";
    }
}
