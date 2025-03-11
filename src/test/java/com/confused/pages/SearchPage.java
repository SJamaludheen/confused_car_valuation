package com.confused.pages;

import com.confused.config.AppConfig;
import com.confused.util.StringUtils;
import com.confused.lib.SeleniumLib;
import com.confused.lib.Wait;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.util.*;

import static com.confused.util.GlobalVariables.failedTests;

public class SearchPage {
    private final SeleniumLib seleniumLib;
    WebDriver driver;
    CarDetailsPage carDetailsPage = new CarDetailsPage(driver);

    private final By cookieAccept = By.id("button-save-all");
    private final By carQuoteButton = By.xpath("//span[text()='Get a car quote']/parent::a");
    private final By findCarButton = By.id("find-vehicle-btn");
    private final By carRegistrationInput = By.xpath("//input[@id='registration-number-input']");
    private final By resultError = By.xpath("//div[@class='error-summary']/h3[@class='error-summary__heading']");
    List<String> registrationNumbers = new ArrayList<>();
    String vehicleRegistrationPattern = "[A-Z]{2}[0-9]{2}\\s*[A-Z]{3}";

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        seleniumLib = new SeleniumLib(driver);
    }

    public void accessConfusedDotCom(String website) {
        Properties config = AppConfig.loadConfigProperties("AppConfig.properties");
        String expectedSite = config.getProperty(website);
        if (expectedSite == null || expectedSite.isEmpty()) {
            Assert.fail(website + " url is empty in the config file. Please provide the url in 'AppConfig.properties' file.");
        }
        accessConfusedDotComCarSearchSite(expectedSite);
        // handling the automated chatbot that pops up intermittently
        try {
            Wait.forElementToBeClickable(driver, driver.findElement(findCarButton));
        } catch (NoSuchElementException e) {
            accessConfusedDotComCarSearchSite(expectedSite);
        }
    }

    public void accessConfusedDotComCarSearchSite(String website) {
        driver.get(website);
        if (seleniumLib.isElementPresent(cookieAccept)) {
            seleniumLib.clickOnElement(cookieAccept);
        }
        Wait.forElementToBePresent(driver, carQuoteButton, 10);
        seleniumLib.clickOnElement(carQuoteButton);
    }

    public void searchForCarRegistrationNumber(String website, String outputFileName) {
        // for each of the registration number read from the input file, search on the website to see if there is any result yielded
        for (String registrationNumber : registrationNumbers) {
            accessConfusedDotCom(website);
            searchSiteForRegistrationNumber(registrationNumber, outputFileName);
        }
    }

    public void searchSiteForRegistrationNumber(String registrationNumber, String outputFileName) {
        seleniumLib.sendValue(carRegistrationInput, registrationNumber);
        seleniumLib.clickOnElement(findCarButton);
        // if car registration number throws an error on the website, capture the error to notify the user at the end of the test
        List<WebElement> errorResults = seleniumLib.getElements(resultError);
        if (errorResults == null) {
            errorResults = new ArrayList<>();
        }
        if (!errorResults.isEmpty()) {
            failedTests.add("Error returned for registration number " + registrationNumber + " : " + errorResults.get(0).getText());
        } else {
            carDetailsPage.compareCarDetails(outputFileName, registrationNumber);
        }
    }

    public void readCarRegistration(String inputFileName) throws IOException {
        String configLocation = System.getProperty("user.dir") + File.separator + "src\\test\\java\\com\\confused\\inputdata" + File.separator;
        BufferedReader reader = new BufferedReader(new FileReader(configLocation + inputFileName));
        String line;
        while ((line = reader.readLine()) != null) {
            // pattern match check to see if registration numbers inputted follow the UK vehicle registration number pattern
            if (!StringUtils.patternFound(line, vehicleRegistrationPattern)) {
                failedTests.add("Car registration number " + line + " from the input file do not match the expected UK vehicle registration number pattern.");
            } else {
                registrationNumbers.add(line.trim());
            }
        }
    }
}