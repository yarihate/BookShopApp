package com.example.BookShopApp.services;

import com.example.BookShopApp.data.dto.RatingDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.book.BookRatingEntity;
import com.example.BookShopApp.data.model.enums.BookRate;
import com.example.BookShopApp.data.repositories.BookRatingRepository;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.services.IBookRatingCount;
import com.example.BookShopApp.data.services.RatingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RatingServiceImplTests {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookRatingRepository bookRatingRepository;

    private final RatingService ratingService;

    @Autowired
    RatingServiceImplTests(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Test
    void addRateIntoOverallRating() {
        final String slug = "bookSlug";
        final Integer value = 3;

        BookEntity book = new BookEntity();
        book.setTitle("Title");

        BookRatingEntity newRate = new BookRatingEntity();
        newRate.setBookRate(BookRate.THREE);
        newRate.setBook(book);

        book.getRating().add(newRate);

        Mockito.doReturn(book).when(bookRepository).findBookBySlug(slug);
        Mockito.doReturn(newRate).when(bookRatingRepository).save(Mockito.any(BookRatingEntity.class));
        Mockito.doReturn(book).when(bookRepository).save(Mockito.any(BookEntity.class));

        ratingService.addRateIntoOverallRating(slug, value);
        Mockito.verify(bookRepository, Mockito.times(1)).findBookBySlug(slug);
        Mockito.verify(bookRatingRepository, Mockito.times(1)).save(Mockito.any(BookRatingEntity.class));
        Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(BookEntity.class));
    }

    @Test
    void receiveBookRating() {
        final String slug = "bookSlug";

        BookEntity book = new BookEntity();
        book.setTitle("Title");

        BookRatingEntity newRate = new BookRatingEntity();
        newRate.setBookRate(BookRate.THREE);
        newRate.setBook(book);

        BookRatingEntity newRate2 = new BookRatingEntity();
        newRate2.setBookRate(BookRate.FOUR);
        newRate2.setBook(book);

        book.getRating().addAll(Arrays.asList(newRate, newRate2));

        IBookRatingCount iBookRatingCount1 = new IBookRatingCount() {
            @Override
            public BookRate getRate() {
                return BookRate.THREE;
            }

            @Override
            public Integer getBookRateCount() {
                return 1;
            }
        };

        IBookRatingCount iBookRatingCount2 = new IBookRatingCount() {
            @Override
            public BookRate getRate() {
                return BookRate.FOUR;
            }

            @Override
            public Integer getBookRateCount() {
                return 1;
            }
        };
        Mockito.doReturn(book).when(bookRepository).findBookBySlug(slug);
        Mockito.doReturn(Arrays.asList(iBookRatingCount1, iBookRatingCount2))
                .when(bookRatingRepository).countBookRatesCount(book.getId());
        Mockito.doReturn(2L).when(bookRatingRepository).countByBookSlug(slug);

        RatingDto ratingDto = ratingService.receiveBookRating(slug);
        assertEquals(2, ratingDto.getRatingOverallCount());
        assertEquals(1, ratingDto.getThreeStarsCount());
        assertEquals(1, ratingDto.getFourStarsCount());
    }
}