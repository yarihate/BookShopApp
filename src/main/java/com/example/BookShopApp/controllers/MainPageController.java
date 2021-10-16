package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.BooksPageDto;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.tag.TagEntity;
import com.example.BookShopApp.data.services.BookService;
import com.example.BookShopApp.data.services.TagService;
import com.example.BookShopApp.errs.EmptySearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MainPageController {

    private final BookService bookService;
    private final TagService tagService;

    @Autowired
    public MainPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }

    @ModelAttribute("recommendedBooks")
    public List<BookEntity> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<BookEntity> recentBooks() {
        return bookService.getBooksByPubDate(null, null, 0, 6).getContent();
    }

    @ModelAttribute("tags")
    public Map<TagEntity, String> tags() {
        List<TagEntity> tags = tagService.getTagsRateData();
        return tagService.createRangedMap(tags);
    }

    @ModelAttribute("popularBooks")
    public List<BookEntity> popularBooks() {
        return bookService.getPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<BookEntity> searchResults() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public String mainPage() {
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

    @GetMapping(value = {"/search", "search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto, Model model) throws EmptySearchException {
      if(searchWordDto != null) {
          model.addAttribute("searchWordDto", searchWordDto);
          model.addAttribute("searchResults", bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
          return "/search/index";
      }else {
          throw new EmptySearchException("Поиск по null невозможен");
      }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit));
    }
}
