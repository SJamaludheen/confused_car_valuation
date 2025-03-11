package com.confused.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.confused.config.SeleniumDriver;

public class Pages {
    protected WebDriver driver;

    //Initialize all the Pages created in this class
    protected SearchPage searchPage;
    protected CarDetailsPage carDetailsPage;

    public Pages(SeleniumDriver driver) {
        this.driver = driver;
        PageObjects();
    }

    public void PageObjects() {
        searchPage = PageFactory.initElements(driver, SearchPage.class);
        carDetailsPage = PageFactory.initElements(driver, CarDetailsPage.class);
    }
}