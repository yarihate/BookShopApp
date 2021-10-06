package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.enums.BookRate;

public interface IBookRatingCount {

    BookRate getRate();

    Integer getBookRateCount();
}