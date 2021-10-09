package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.ApiResponse;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.errs.BookStoreApiWrongParameterException;
import com.example.BookShopApp.errs.EmptySearchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleMissingServletRequestParameterException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Missing required parameters", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookStoreApiWrongParameterException.class)
    public ResponseEntity<ApiResponse<BookEntity>> handleBookstoreApiWrongParameterException(Exception ex) {
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST, "Bad parameters", ex),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", ex);
        return "redirect:/";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UsernameNotFoundError", ex);
        return "redirect:/signin";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("UsernameNotFoundError", ex);
        redirectAttributes.addFlashAttribute("IncorrectPasswordError", ex);
        return "redirect:/signin";
    }
}
