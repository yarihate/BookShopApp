package com.example.BookShopApp.errs;

public class BookStoreApiWrongParameterException extends Exception{
    public BookStoreApiWrongParameterException(String message) {
        super(message);
    }
}
