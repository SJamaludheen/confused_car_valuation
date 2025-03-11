package com.confused.pages;

import com.confused.config.CarDetails;
import com.confused.lib.SeleniumLib;
import com.confused.lib.Wait;
import com.confused.util.Debugger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.util.*;
import static com.confused.util.GlobalVariables.failedTests;

public class CarDetailsPage {
    private final SeleniumLib seleniumLib;
    WebDriver driver;
    private final By resultsPageTitle = By.id("registration-lookup-summary-title");
    private final By year = By.xpath("//div[@class='panel']/p[text()='Year: ']");
    private final By make = By.xpath("//div[@class='panel']/p[text()='Manufacturer: ']");
    private final By model = By.xpath("//div[@class='panel']/p[text()='Model: ']");
    private final By registration = By.xpath("//div[@class='panel']/p[text()='Registration: ']");

    public CarDetailsPage(WebDriver driver) {
        this.driver = driver;
        seleniumLib = new SeleniumLib(driver);
    }

    public void waitForPageLoad() {
        Wait.forElementToBePresent(driver, resultsPageTitle, 10);
    }

    public void checkForComparisonResults() {
        // Log the error messages and comparison mismatch results, if any, before failing the test
        if (!failedTests.isEmpty()) {
            for (String failure : failedTests) {
                Debugger.println(failure);
            }
            Assert.fail("Some tests failed. Please check the logs.");
        }
    }

    public void compareCarDetails(String filename, String registrationNumber) {
        // for every car record fond in the output file, data comparison is performed and in case of mismatches, the list is updated to be logged at the end of the test
        boolean carRecordFound = readCarDetailsFromOutputFileForTheGivenCar(filename, registrationNumber);
        if (carRecordFound) {
            String actualRegistrationValue = seleniumLib.getText(registration).split(": ")[1];
            String actualMakeValue = seleniumLib.getText(make).split(": ")[1];
            String actualModelValue = seleniumLib.getText(model).split(": ")[1];
            String actualYearValue = seleniumLib.getText(year).split(": ")[1];
            if (!CarDetails.getRegistration().equals(actualRegistrationValue)) {
                failedTests.add("Expected 'Registration' of the car " + registrationNumber + " do not match the actual registration value on the website. Expected : " + CarDetails.getRegistration() + ". Actual : " + actualRegistrationValue + ".");
            }
            if (!CarDetails.getMake().equals(actualMakeValue)) {
                failedTests.add("Expected 'Make' of the car " + registrationNumber + " do not match the actual make value on the website. Expected : " + CarDetails.getMake() + ". Actual : " + actualMakeValue + ".");
            }
            if (!CarDetails.getModel().equals(actualModelValue)) {
                failedTests.add("Expected 'Model' of the car " + registrationNumber + " do not match the actual model value on the website. Expected : " + CarDetails.getModel() + ". Actual : " + actualModelValue + ".");
            }
            if (!CarDetails.getYear().equals(actualYearValue)) {
                failedTests.add("Expected 'Year' of the car " + registrationNumber + " do not match the actual year value on the website. Expected : " + CarDetails.getYear() + ". Actual : " + actualYearValue + ".");
            }
        } else {
            failedTests.add("No car records/details found for " + registrationNumber + " in the '" + filename + "' file.");
        }
    }

    public boolean readCarDetailsFromOutputFileForTheGivenCar(String filename, String registration) {
        Scanner fileScanner;
        String configLocation = System.getProperty("user.dir") + File.separator + "src\\test\\java\\com\\confused\\inputdata" + File.separator;
        try {
            fileScanner = new Scanner(new File(configLocation + filename));
            String line = "";
            String[] details = null;
            String[] title = null;
            HashMap<String, String> hm = new HashMap<>();
            int lineCount = 0;
            boolean carRecordFound = false;
            while (fileScanner.hasNextLine()) {
                lineCount++;
                if (lineCount == 1) {
                    line = fileScanner.nextLine();
                    if (line.contains("VARIANT_REG") && line.contains("MAKE") && line.contains("MODEL") && line.contains("YEAR")) {
                        title = line.split(",");
                        continue;
                    } else {
                        Assert.fail("Expected title in the '" + filename + "' - 'VARIANT_REG,MAKE,MODEL,YEAR' is not present. This should be the first line in the file.");
                    }
                }
                line = fileScanner.nextLine();
                details = line.split(",");
                if (details.length != title.length) {
                    Assert.fail("Each car record in the '" + filename + "' file should contain 'VARIANT_REG,MAKE,MODEL,YEAR'. Actual values found : " + line + ".");
                }
                //Initialising
                for (int i = 0; i < title.length; i++) {
                    hm.put(title[i], details[i]);
                }
                CarDetails carDetails = new CarDetails();
                if (line.contains(registration) || line.contains(registration.replace(" ", "")) || line.replace(" ", "").contains(registration)) {
                    carDetails.setRegistration(hm.get("VARIANT_REG"));
                    carDetails.setMake(hm.get("MAKE"));
                    carDetails.setModel(hm.get("MODEL"));
                    carDetails.setYear(hm.get("YEAR"));
                    return true;
                }
            }
            if (lineCount < 2) {
                Assert.fail("No car records/details specified in '" + filename + "' file.");
            }
            if (!carRecordFound) {
                return false;
            }
        } catch (Exception e) {
            Assert.fail("Exception while trying to access / read '" + filename + "' file from the specified location : " + configLocation + ". Exception : " + e);
        }
        return true;
    }
}