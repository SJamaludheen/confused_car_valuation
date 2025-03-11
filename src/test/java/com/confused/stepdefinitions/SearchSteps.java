package com.confused.stepdefinitions;

import com.confused.pages.Pages;
import com.confused.config.SeleniumDriver;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import java.io.IOException;

public class SearchSteps extends Pages {

    public SearchSteps(SeleniumDriver driver) {
        super(driver);
    }

    @Given("^the user reads the car registration numbers from the given input file (.*)$")
    public void theUserReadsTheCarRegistrationNumbersFromTheGivenInputFile(String inputFileName) throws IOException {
        searchPage.readCarRegistration(inputFileName);
    }

    @When("^the user searches for each car on the car valuation website (.*) to compare with the expected details in the output file (.*)$")
    public void theUserSearchesForEachCarOnTheCarValuationWebsite(String website, String fileName) {
        searchPage.searchForCarRegistrationNumber(website, fileName);
    }
}