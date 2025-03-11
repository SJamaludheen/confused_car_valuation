package com.confused.stepdefinitions;

import com.confused.config.SeleniumDriver;
import com.confused.pages.Pages;
import cucumber.api.java.en.Then;

public class CarDetailsSteps extends Pages {

    public CarDetailsSteps(SeleniumDriver driver) {
        super(driver);
    }

    @Then("^the user can see comparison results for each of the car$")
    public void theUserCanSeeComparisonResultsForEachOfTheCar() {
        carDetailsPage.checkForComparisonResults();
    }
}