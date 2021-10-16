package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.ChangeUserDataForm;
import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import com.example.BookShopApp.security.BookstoreUserDetails;
import com.example.BookShopApp.security.ContactConfirmationPayload;
import com.example.BookShopApp.security.ContactConfirmationResponse;
import com.example.BookShopApp.security.RegistrationForm;
import com.example.BookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserRegister {
    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;


    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;

    }

    public BookstoreUser registerNewUser(RegistrationForm registrationForm) {
        if (bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null) {
            BookstoreUser user = new BookstoreUser();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            return bookstoreUserRepository.save(user);
        }
        return null;
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public BookstoreUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")) {
            return null;
        }
        if (principal instanceof BookstoreUserDetails) {
            return bookstoreUserRepository.findBookstoreUserByEmail(((BookstoreUserDetails) principal).getEmail());
        } else if (principal instanceof DefaultOAuth2User) {
            BookstoreUser bookStoreUser = new BookstoreUser();
            bookStoreUser.setName(((DefaultOAuth2User) principal).getAttribute("name"));
            bookStoreUser.setEmail(((DefaultOAuth2User) principal).getAttribute("email"));
            return bookStoreUser;
        }
        BookstoreUserDetails bookStoreUserDetails = (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookStoreUserDetails.getBookstoreUser();
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
                payload.getCode()));
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public boolean applyUserDataChanges(ChangeUserDataForm changeUserDataForm, String name) {
        BookstoreUser user = bookstoreUserRepository.findBookstoreUserByName(name);
        if (changeUserDataForm.getName() != null) {
            user.setName(changeUserDataForm.getName());
        }
        if (changeUserDataForm.getMail() != null) {
            user.setEmail(changeUserDataForm.getMail());
        }
        if (changeUserDataForm.getPhone() != null) {
            user.setPhone(changeUserDataForm.getPhone());
        }
        if (changeUserDataForm.getPassword() != null && !changeUserDataForm.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(changeUserDataForm.getPassword()));
        }
        bookstoreUserRepository.save(user);
        return true;
    }
}
