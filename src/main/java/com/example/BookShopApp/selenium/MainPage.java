package com.example.BookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainPage {

    private String url = "http://localhost:8080/";

    private ChromeDriver driver;

    public MainPage(ChromeDriver driver) {
        this.driver = driver;
    }

    public MainPage callPage() {
        driver.get(url);
        return this;
    }

    public MainPage pause() throws InterruptedException {
        Thread.sleep(7000);
        return this;
    }

    public MainPage setUpSearchQuery(String query) {
        WebElement element = driver.findElement(By.id("query"));
        element.sendKeys(query);
        return this;
    }

    public MainPage submit() {
        WebElement element = driver.findElement(By.id("search"));
        element.submit();
        return this;
    }

    public MainPage callGenresPage() {
        WebElement element = driver.findElement(By.linkText("Genres"));
        element.click();
        return this;
    }

    public MainPage callRecentPage() {
        WebElement element = driver.findElement(By.linkText("Recent"));
        element.click();
        return this;
    }

    public MainPage callPopular() {
        WebElement element = driver.findElement(By.linkText("Popular"));
        element.click();
        return this;
    }

    public MainPage callAuthors() {
        WebElement element = driver.findElement(By.linkText("Authors"));
        element.click();
        return this;
    }
}