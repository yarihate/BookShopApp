package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.BookRateValue;
import com.example.BookShopApp.data.dto.BookStatusDto;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.enums.BookStatus;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;


@Controller
@RequestMapping("books")
public class BookShopCartController {
    //todo ask the question
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    private final BookRepository bookRepository;
    private final RatingService ratingService;

    @ModelAttribute(name = "bookCart")
    public List<BookEntity> bookCart() {
        return new ArrayList<>();
    }
    @ModelAttribute(name = "bookPostponedList")
    public List<BookEntity> BookPostponedList() {
        return new ArrayList<>();
    }


    private static final String POSTPONED_CONTENTS_COOKIE_NAME = "postponedContents";

    private static final String CART_CONTENTS_COOKIE_NAME = "cartContents";
    @Autowired
    public BookShopCartController(BookRepository bookRepository, RatingService ratingService) {
        this.bookRepository = bookRepository;
        this.ratingService = ratingService;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents, Model model){
        if(cartContents == null || cartContents.isBlank()){
            model.addAttribute("isCartEmpty", true);
        }else{
            model.addAttribute("isCartEmpty", false);
            cartContents = cartContents.startsWith("/") ? cartContents.substring(1) : cartContents;
            cartContents = cartContents.endsWith("/") ? cartContents.substring(0, cartContents.length() - 1) : cartContents;
            String[] cookieSlugs = cartContents.split("/");
            model.addAttribute("bookCart", bookRepository.findBooksBySlugIn(cookieSlugs));
        }
        return "cart";
    }

    @GetMapping("/postponed")
    public String handlePostponedListRequest(@CookieValue(name = "postponedContents", required = false) String postponedContents,
                                             Model model) {
        if (postponedContents == null || postponedContents.equals("")) {
            model.addAttribute("isPostponedListEmpty", true);
        } else {
            model.addAttribute("isPostponedListEmpty", false);
            String[] arrayWithBookSlugsFromCookie = createArrayWithBookSlugsFromCookie(postponedContents);
            List<BookEntity> booksFromCookiesSlugs = bookRepository.findBooksBySlugIn(arrayWithBookSlugsFromCookie);
            model.addAttribute("bookPostponedList", booksFromCookiesSlugs);
        }
        return "postponed";
    }

    @PostMapping(path = "/changeBookStatus/{slug}")
    public String handleChangeBookStatus(@PathVariable String slug,
                                         @CookieValue(name = "cartContents", required = false) String cartContents,
                                         @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                         HttpServletResponse response, Model model, @RequestBody BookStatusDto bookStatusDto) {
        if (bookStatusDto.getStatus().equals(BookStatus.CART.name())) {
            addBookToCookieContentBySlug(cartContents, CART_CONTENTS_COOKIE_NAME, slug, response, model);
          //  changeBookStatus(slug, BookStatus.CART);
        } else if (bookStatusDto.getStatus().equals(BookStatus.KEPT.name())) {
            addBookToCookieContentBySlug(postponedContents, POSTPONED_CONTENTS_COOKIE_NAME, slug, response, model);
          //  changeBookStatus(slug, BookStatus.KEPT);
        }
        return "redirect:/books/" + slug;
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartByRequest(@PathVariable("slug") String slug, Model model,
                                                    @CookieValue(name = "cartContents", required = false)
                                                            String cartContents, HttpServletResponse response) {
        if (cartContents != null && !cartContents.isBlank()) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
            cookieBooks.remove(slug);
            Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
            cookie.setPath("/books");
            response.addCookie(cookie);
            model.addAttribute("isCartEmpty", false);
        } else {
            model.addAttribute("isCartEmpty", true);
        }
        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/rating/{slug}")
    public String addRatingValue(@RequestBody BookRateValue bookRateValue, @PathVariable("slug") String slug) {
        ratingService.addRateIntoOverallRating(slug, bookRateValue.getValue());
        return "redirect:/books/" + slug;
    }

    private void addBookToCookieContentBySlug(String stringFromCookie, String cookieName, String bookSlug,
                                              HttpServletResponse response, Model model) {
        if (stringFromCookie == null || stringFromCookie.equals("")) {
            setCookieAndDefineEmptyAttr(cookieName, bookSlug, response, model);
        } else if (!stringFromCookie.contains(bookSlug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(stringFromCookie).add(bookSlug);
            setCookieAndDefineEmptyAttr(cookieName, stringJoiner.toString(), response, model);
        }
    }

    private void setCookieAndDefineEmptyAttr(String cookieName, String cookieContent,
                                             HttpServletResponse response, Model model) {
        Cookie cookie = new Cookie(cookieName, cookieContent);
        cookie.setPath("/books");
        response.addCookie(cookie);
        model.addAttribute("is" + cookieName.substring(0, 1).toUpperCase() +
                cookieName.substring(1) + "Empty", false);
    }

//    private void changeBookStatus(String bookSlug, BookStatus bookStatus) {
//        BookEntity bookBySlug = bookRepository.findBookBySlug(bookSlug);
//        bookBySlug.getStatuses().add(bookStatus);
//        bookRepository.save(bookBySlug);
//    }

    private static String[] createArrayWithBookSlugsFromCookie(String stringFromCookie) {
        stringFromCookie = stringFromCookie.startsWith("/") ? stringFromCookie.substring(1) : stringFromCookie;
        stringFromCookie = stringFromCookie.endsWith("/") ? stringFromCookie.substring(0, stringFromCookie.length() - 1)
                : stringFromCookie;
        return stringFromCookie.split("/");
    }
}
