package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.genre.GenreEntity;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private BookRepository bookRepository;
    private GenreRepository genreRepository;

    @Autowired
    public BookService(BookRepository bookRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByAuthorId(Integer authorId) {
        return bookRepository.findBooksByAuthor_Id(authorId);
    }

    public List<BookEntity> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<BookEntity> getBooksByTitle(String title) {
        return bookRepository.findBooksByTitleContaining(title);
    }

    public List<BookEntity> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
    }

    public List<BookEntity> getBooksWithPrice(Integer price) {
        return bookRepository.findBooksByPriceOldIs(price);
    }

    public List<BookEntity> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<BookEntity> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, nextPage);
    }

    public Page<BookEntity> getBooksByPubDate(LocalDate from, LocalDate to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateIsBetweenOrderByPubDateDescIdAsc(from, to, nextPage);
    }

    public Page<BookEntity> getPopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPopularBooks(nextPage);
    }

    public Page<BookEntity> getBooksByTag(Long id, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTag(id, nextPage);
    }

    public Page<BookEntity> receivePageOfBooksWithSpecificGenre(String genreName, Integer offset, Integer limit) {
        List<Integer> genreIdsList = new ArrayList<>();
        Integer firstLevelGenreId = genreRepository.findByNameIs(genreName).getId();
        genreIdsList.add(firstLevelGenreId);
        List<Integer> secondLevelGenreIdList = genreRepository.findByParentIdIs(firstLevelGenreId)
                .stream()
                .map(GenreEntity::getId)
                .collect(Collectors.toList());
        if (!secondLevelGenreIdList.isEmpty()) {
            genreIdsList.addAll(secondLevelGenreIdList);
            List<Integer> thirdLevelGenreIdList = secondLevelGenreIdList
                    .stream()
                    .map(genreRepository::findByParentIdIs)
                    .flatMap(Collection::stream)
                    .map(GenreEntity::getId)
                    .collect(Collectors.toList());
            genreIdsList.addAll(thirdLevelGenreIdList);
        }
        return bookRepository.findByGenresIdIn(genreIdsList, PageRequest.of(offset, limit));
    }
}
