package com.confused.stepdefinitions;

import com.confused.config.SeleniumDriver;
import com.confused.pages.Pages;
import com.confused.lib.SeleniumLib;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestHooks  extends Pages {
    SeleniumLib seleniumLib;
    public static String currentTagName = "";
    public static String currentTags = "";
    public static String currentFeature = "";
    public static String tempTagName = "";
    public static boolean newScenarioFeature = false;

    public TestHooks(SeleniumDriver driver) {
        super(driver);
        seleniumLib = new SeleniumLib(driver);
    }

    @Before(order=0)
    public void beforeTestScenario() {
        SeleniumLib.clearCookies();
    }

    @Before(order=1)
    public void beginningOfTest(Scenario scenario) {
        currentTagName = scenario.getSourceTagNames().toString();
        currentTags = scenario.getSourceTagNames().toString();
        currentFeature = scenario.getId().split(";")[0];
        if (tempTagName.isEmpty() || !(tempTagName.equalsIgnoreCase(currentTagName))) {
            tempTagName = currentTagName;
            newScenarioFeature = true;
        } else {
            newScenarioFeature = false;
        }
    }

    @After(order=1)
    public void tearDown(Scenario scenario) {
        String scenarioStatus =  scenario.getStatus();
        if (!scenarioStatus.equalsIgnoreCase("PASSED")) {
            scenario.embed(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES), "image/png");
        }
    }
    @After(order=0)
    public void afterScenario() {
    }
}