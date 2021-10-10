package com.example.BookShopApp.selenium;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class MainPageSeleniumTests extends AbstractSeleniumTest {

    @Test
    public void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();
        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

//    @Test
//    public void testMainPageSearchByQuery() throws InterruptedException {
//        MainPage mainPage = new MainPage(driver);
//        mainPage
//                .callPage()
//                .pause()
//                .setUpSearchQuery("Wisegirls")
//                .pause()
//                .submit()
//                .pause();
//
//        assertTrue(driver.getPageSource().contains("Wisegirls"));
//    }

    @Test
    public void invokeGenres() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .callGenresPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Жанр"));
    }

    @Test
    public void invokeRecent() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .callRecentPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Новинки"));
    }

    @Test
    public void invokePopular() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .callPopular()
                .pause();
        assertTrue(driver.getPageSource().contains("Популярное"));
    }

    @Test
    public void invokeAuthors() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .callAuthors()
                .pause();
        assertTrue(driver.getPageSource().contains("Authors"));
    }

}
