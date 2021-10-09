package com.example.BookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MainPageSeleniumTests {

   static ChromeDriver driver;

    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Lera\\Downloads\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.MINUTES);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

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
