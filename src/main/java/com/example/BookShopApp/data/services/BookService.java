package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.google.api.books.Item;
import com.example.BookShopApp.data.google.api.books.Root;
import com.example.BookShopApp.data.model.author.AuthorEntity;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.genre.GenreEntity;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.repositories.GenreRepository;
import com.example.BookShopApp.errs.BookStoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private BookRepository bookRepository;
    private GenreRepository genreRepository;
    private RestTemplate restTemplate;

    @Autowired
    public BookService(BookRepository bookRepository, GenreRepository genreRepository, RestTemplate restTemplate) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.restTemplate = restTemplate;
    }

    public List<BookEntity> getBooksByAuthorId(Integer authorId) {
        return bookRepository.findBooksByAuthor_Id(authorId);
    }

    public List<BookEntity> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<BookEntity> getBooksByTitle(String title) throws BookStoreApiWrongParameterException {
        if (title.isBlank()) {
            throw new BookStoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<BookEntity> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookStoreApiWrongParameterException("No data found with specified parameters");
            }
        }
    }

    public List<BookEntity> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceOldBetween(min, max);
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

    @Value("${google.books.api.key}")
    private String apiKey;

    public List<BookEntity> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit) {
        String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes" +
                "?q=" + searchWord +
                "&key=" + apiKey +
                "&filter=paid-ebooks" +
                "&startIndex=" + offset +
                "&maxResult=" + limit;

        Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
        ArrayList<BookEntity> list = new ArrayList<>();
        if (root != null) {
            for (Item item : root.getItems()) {
                BookEntity book = new BookEntity();
                if (item.getVolumeInfo() != null) {
                    book.setAuthor(new AuthorEntity(item.getVolumeInfo().getAuthors()));
                    book.setTitle(item.getVolumeInfo().getTitle());
                    book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
                }
                if (item.getSaleInfo() != null) {
                    book.setPrice(item.getSaleInfo().getRetailPrice().getAmount());
                    Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
                    book.setPriceOld(oldPrice.intValue());
                }
                list.add(book);
            }
        }
        return list;
    }
}
