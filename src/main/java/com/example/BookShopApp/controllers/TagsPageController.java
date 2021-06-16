package com.example.BookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tags")
public class TagsPageController {

    @GetMapping()
    public String tagsPage() {
        return "tags/index";
    }
}
