package com.example.BookShopApp.selenium;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class BookReviewSeleniumTest extends AbstractSeleniumTest {
    

    @Test
    public void testReviewAdditionAndThenLikeIt() throws InterruptedException {
        BookPage bookPage = new BookPage(driver);
        bookPage.callSignInPage()
                .pause()
                .changeToEmailMode()
                .pause()
                .setUpEmail("test@mail.com")
                .nextButton()
                .pause()
                .setUpPassword("12345678")
                .pause()
                .comeInButton()
                .pause()
                .callSpecificBookPage()
                .pause()
                .addReviewComment("test comments")
                .pause()
                .submit()
                .pause()
                .reloadPage()
                .pause()
                .addLikeToReview()
                .pause()
                .reloadPage()
                .pause()
                .addLikeToReview()
                .reloadPage()
                .pause();

        assertTrue(driver.getPageSource().contains("test comments"));
    }
}

