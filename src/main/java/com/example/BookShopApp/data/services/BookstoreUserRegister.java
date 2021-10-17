package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.ChangePasswordForm;
import com.example.BookShopApp.data.ChangeUserDataForm;
import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.model.UserDataEdition;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import com.example.BookShopApp.data.repositories.UserDataEditionRepository;
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
import org.springframework.ui.Model;

import java.util.UUID;

@Service
public class BookstoreUserRegister {
    private final BookstoreUserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final UserDataEditionRepository userDataEditionRepository;

    @Autowired
    public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService, JWTUtil jwtUtil, UserDataEditionRepository userDataEditionRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.userDataEditionRepository = userDataEditionRepository;
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

    public boolean changePassword(ChangePasswordForm changePasswordForm, Model model) {
        BookstoreUser userByEmail = bookstoreUserRepository.findBookstoreUserByEmail(changePasswordForm.getEmail());
        BookstoreUser userByPhone = bookstoreUserRepository.findBookstoreUserByPhone(changePasswordForm.getPhone());
        if (userByEmail.equals(userByPhone) && passwordEncoder.matches(changePasswordForm.getOldPassword(), userByEmail.getPassword())) {
            userByEmail.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
            bookstoreUserRepository.save(userByEmail);
            return true;
        } else {
            model.addAttribute("changePassError", true);
            return false;
        }
    }

    public void saveTempUserDataChanges(ChangeUserDataForm changeUserDataForm, String changeUuid, String userEmail) {
        UserDataEdition userDataEdition = new UserDataEdition(changeUserDataForm, changeUuid, userEmail);
        userDataEditionRepository.save(userDataEdition);
    }

    public boolean applyUserDataChanges(String uuid) {
        UserDataEdition userDataEditionFromDB = userDataEditionRepository.findById(UUID.fromString(uuid)).orElse(null);
        if (userDataEditionFromDB != null) {
            BookstoreUser user = bookstoreUserRepository.findBookstoreUserByEmail(userDataEditionFromDB.getUserDetailEmail());
            if (userDataEditionFromDB.getName() != null) {
                user.setName(userDataEditionFromDB.getName());
            }
            if (userDataEditionFromDB.getMail() != null) {
                user.setEmail(userDataEditionFromDB.getMail());
            }
            if (userDataEditionFromDB.getPhone() != null) {
                user.setPhone(userDataEditionFromDB.getPhone());
            }
            if (userDataEditionFromDB.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDataEditionFromDB.getPassword()));
            }
            bookstoreUserRepository.save(user);
            return true;
        }
        return false;
    }
}
