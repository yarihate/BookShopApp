package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.PaymentResponse;
import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.payments.BalanceTransactionEntity;
import com.example.BookShopApp.data.repositories.BalanceTransactionRepository;
import com.example.BookShopApp.data.repositories.BookstoreUserRepository;
import com.example.BookShopApp.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Value("${robokassa.merchant.login}")
    private String merchantLogin;

    @Value("${robokassa.pass.first.test}")
    private String firstTestPass;

    private final BookstoreUserRepository bookstoreUserRepository;
    private final BalanceTransactionRepository balanceTransactionRepository;

    @Autowired
    public PaymentService(BookstoreUserRepository bookstoreUserRepository, BalanceTransactionRepository balanceTransactionRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
        this.balanceTransactionRepository = balanceTransactionRepository;
    }

    public String getPaymentUrl(List<BookEntity> booksFromCookieSlugs) throws NoSuchAlgorithmException {
        Double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(BookEntity::getPrice).sum();
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5"; //just for testing TODO order indexing later
        md.update((merchantLogin + ":" + paymentSumTotal.toString() + ":" + invId + ":" + firstTestPass).getBytes());
        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&IndId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + paymentSumTotal.toString() +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    private String getPaymentUrl(Double sum) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        String invId = "5"; //just for Testing
        md.update((merchantLogin + ":" + sum.toString() + ":" + invId + ":" + firstTestPass).getBytes());
        return "https://auth.robokassa.ru/Merchant/Index.aspx" +
                "?MerchantLogin=" + merchantLogin +
                "&InvId=" + invId +
                "&Culture=ru" +
                "&Encoding=utf-8" +
                "&OutSum=" + sum.toString() +
                "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
                "&IsTest=1";
    }

    @Transactional
    public String topUpUserAccount(Double sum, Principal principal) throws NoSuchAlgorithmException {
        BookstoreUser user;
        if (((UsernamePasswordAuthenticationToken) principal).getPrincipal() instanceof BookstoreUserDetails) {
            user = bookstoreUserRepository.findBookstoreUserByEmail(((BookstoreUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal()).getEmail());
        } else {
            user = bookstoreUserRepository.findBookstoreUserByName(principal.getName());
        }
        user.setBalance(user.getBalance() + sum);
        bookstoreUserRepository.save(user);

        createTransaction(user, sum, "Пополнение счета в размере: " + sum);

        return getPaymentUrl(sum);
    }

    public PaymentResponse payFromUserAccount(List<BookEntity> booksFromCookieSlugs) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BookstoreUser user;

        if (principal instanceof BookstoreUserDetails && ((BookstoreUserDetails) principal).getUsername().contains("@")) {
            user = bookstoreUserRepository.findBookstoreUserByEmail(((BookstoreUserDetails) principal).getUsername());
        } else {
            return new PaymentResponse(false, "Пожалуйста авторизируйтесь!");
        }
        Double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(BookEntity::getPrice).sum();
        if (user.getBalance() < paymentSumTotal) {
            return new PaymentResponse(false, "Недостаточно денежных средств");
        }

        createTransaction(user, paymentSumTotal, "Списание средств в размере: " + paymentSumTotal);

        user.setBalance(user.getBalance() - paymentSumTotal);
        bookstoreUserRepository.save(user);
        return new PaymentResponse(true, "Успешное списание денежных средств");
    }

    private void createTransaction(BookstoreUser user, Double value, String message) {
        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setUser(user);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setDescription(message);
        transaction.setValue(value);
        balanceTransactionRepository.save(transaction);
    }
}
