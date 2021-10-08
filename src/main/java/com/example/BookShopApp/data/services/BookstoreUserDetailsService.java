package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import com.example.BookShopApp.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {
    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if (bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }

    public UserDetails loadUserByUsernameJwt(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByName(s);
        if (bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
