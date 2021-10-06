package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.dto.RatingDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.book.BookRatingEntity;
import com.example.BookShopApp.data.model.enums.BookRate;
import com.example.BookShopApp.data.repositories.BookRatingRepository;
import com.example.BookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService {
    private final BookRepository bookRepository;
    private final BookRatingRepository bookRatingRepository;

    @Autowired
    public RatingService(BookRepository bookRepository, BookRatingRepository bookRatingRepository) {
        this.bookRepository = bookRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    public void addRateIntoOverallRating(String slug, Integer value) {
        BookEntity book = bookRepository.findBookBySlug(slug);
        BookRatingEntity newRate = new BookRatingEntity();
        newRate.setDateTime(LocalDateTime.now());
        newRate.setBook(book);
        switch (value) {
            case (1):
                newRate.setBookRate(BookRate.ONE);
                break;
            case (2):
                newRate.setBookRate(BookRate.TWO);
                break;
            case (3):
                newRate.setBookRate(BookRate.THREE);
                break;
            case (4):
                newRate.setBookRate(BookRate.FOUR);
                break;
            case (5):
                newRate.setBookRate(BookRate.FIVE);
                break;
            default:
                break;
        }
        BookRatingEntity savedRating = bookRatingRepository.save(newRate);
        book.getRating().add(savedRating);
        bookRepository.save(book);
    }

    public RatingDto receiveBookRating(String slug) {
        BookEntity bookBySlug = bookRepository.findBookBySlug(slug);
        List<IBookRatingCount> ratingCount = bookRatingRepository.countBookRatesCount(bookBySlug.getId());
        RatingDto ratingDto = new RatingDto();
        ratingDto.setRatingOverallCount((int) bookRatingRepository.countByBookSlug(slug));
        ratingCount.forEach(bookRatingCount -> {
            switch (bookRatingCount.getRate()) {
                case ONE:
                    ratingDto.setOneStarCount(bookRatingCount.getBookRateCount());
                    break;
                case TWO:
                    ratingDto.setTwoStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case THREE:
                    ratingDto.setThreeStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case FOUR:
                    ratingDto.setFourStarsCount(bookRatingCount.getBookRateCount());
                    break;
                case FIVE:
                    ratingDto.setFiveStarsCount(bookRatingCount.getBookRateCount());
                    break;
                default:
                    break;
            }
        });
        return ratingDto;
    }
}
