package com.example.BookShopApp.selenium;

import com.example.BookShopApp.data.model.BookstoreUser;
import com.example.BookShopApp.data.services.BookstoreUserRegister;
import com.example.BookShopApp.security.BookstoreUserDetails;
import com.example.BookShopApp.security.ContactConfirmationPayload;
import com.example.BookShopApp.security.ContactConfirmationResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class BookPage {
    @Autowired
    private BookstoreUserRegister bookstoreUserRegister;
    private String sighInPageUrl = "http://localhost:8080/signin";
    private String bookPageUrl = "http://localhost:8080/books/book-wsk-744";

    private ChromeDriver driver;

    public BookPage(ChromeDriver driver) {
        this.driver = driver;
    }

    public BookPage callSignInPage() {
        driver.get(sighInPageUrl);
        return this;
    }

    public BookPage pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public BookPage changeToEmailMode() {
        WebElement element = driver.findElement(By.id("emailButton"));
        element.click();
        return this;
    }

    public BookPage setUpEmail(String email) {
        WebElement element = driver.findElement(By.id("mail"));
        element.sendKeys(email);
        return this;
    }

    public BookPage nextButton() {
        WebElement element = driver.findElement(By.id("sendauth"));
        element.click();
        return this;
    }

    public BookPage setUpPassword(String password) {
        WebElement element = driver.findElement(By.id("mailcode"));
        element.sendKeys(password);
        return this;
    }

    public BookPage comeInButton() {
        WebElement element = driver.findElement(By.id("toComeInMail"));
        element.click();
        return this;
    }

    public BookPage callSpecificBookPage() {
        driver.get(bookPageUrl);
        return this;
    }

    public BookPage addReviewComment(String comment) {
        WebElement element = driver.findElement(By.id("commentarea"));
        element.sendKeys(comment);
        return this;
    }

    public BookPage submit() {
        WebElement element = driver.findElement(By.id("submitcomment"));
        element.submit();
        return this;
    }

    public BookPage reloadPage() {
        return callSpecificBookPage();
    }

    public BookPage addLikeToReview() {
        WebElement element = driver.findElement(By.id("likebutton"));
        element.click();
        return this;
    }
}
