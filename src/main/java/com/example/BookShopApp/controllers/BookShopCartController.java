package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.BookRateValue;
import com.example.BookShopApp.data.PaymentResponse;
import com.example.BookShopApp.data.Sum;
import com.example.BookShopApp.data.dto.BookReviewRateValue;
import com.example.BookShopApp.data.dto.BookStatusDto;
import com.example.BookShopApp.data.dto.SearchWordDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.enums.BookStatus;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.services.PaymentService;
import com.example.BookShopApp.data.services.RatingService;
import com.example.BookShopApp.data.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
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
    private final ReviewService reviewService;
    private final PaymentService paymentService;

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
    public BookShopCartController(BookRepository bookRepository, RatingService ratingService, ReviewService reviewService, PaymentService paymentService) {
        this.bookRepository = bookRepository;
        this.ratingService = ratingService;
        this.reviewService = reviewService;
        this.paymentService = paymentService;
    }

    @GetMapping("/cart")
    public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents, Model model) {
        if (cartContents == null || cartContents.isBlank()) {
            model.addAttribute("isCartEmpty", true);
        } else {
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
        } else if (bookStatusDto.getStatus().equals(BookStatus.KEPT.name())) {
            addBookToCookieContentBySlug(postponedContents, POSTPONED_CONTENTS_COOKIE_NAME, slug, response, model);
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

    @PostMapping("/{slug}/review/save")
    public String saveNewBookReview(@RequestParam String comment,
                                    @PathVariable("slug") String slug) {
        reviewService.addNewReview(slug, comment);
        return "redirect:/books/" + slug;
    }

    @PostMapping("/rateBookReview/{bookSlug}")
    public String handleBookReviewRateChanging(@RequestBody BookReviewRateValue reviewRateValue,
                                               @PathVariable("bookSlug") String bookSlug) {
        reviewService.changeBookReviewRate(reviewRateValue.getReviewid(), reviewRateValue.getValue());
        return "redirect:/books/" + bookSlug;
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


    private static String[] createArrayWithBookSlugsFromCookie(String stringFromCookie) {
        stringFromCookie = stringFromCookie.startsWith("/") ? stringFromCookie.substring(1) : stringFromCookie;
        stringFromCookie = stringFromCookie.endsWith("/") ? stringFromCookie.substring(0, stringFromCookie.length() - 1)
                : stringFromCookie;
        return stringFromCookie.split("/");
    }

    @GetMapping("/pay")
    public String handlePay(@CookieValue(name = "cartContents", required = false) String cartContents,
                            @CookieValue(name = "postponedContents", required = false) String postponedContents,
                            HttpServletResponse response, RedirectAttributes redirectAttributes) {
        String bookContent;
        if (cartContents != null) {
            bookContent = cartContents;
        } else {
            bookContent = postponedContents;
        }
        String[] cookieSlugs = createArrayWithBookSlugsFromCookie(bookContent);
        List<BookEntity> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);

        PaymentResponse paymentResponse = paymentService.payFromUserAccount(booksFromCookieSlugs);
        if (paymentResponse.isSuccess()) {
            Cookie cookie;
            if (cartContents != null) {
                cookie = new Cookie("cartContents", null);
            } else {
                cookie = new Cookie("postponedContents", null);
            }
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            redirectAttributes.addFlashAttribute("paymentMessage", paymentResponse.getMessage());
            return "redirect:/";

        } else {
            redirectAttributes.addFlashAttribute("paymentMessage", paymentResponse.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/topupaccount")
    public RedirectView handleTopUpAccount(@RequestBody Sum sum, Principal principal) throws NoSuchAlgorithmException {
        String paymentUrl = paymentService.topUpUserAccount(sum.getSum(), principal);
        return new RedirectView(paymentUrl);
    }
}
