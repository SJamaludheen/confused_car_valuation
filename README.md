## Selenium-Cucumber-Java test automation framework

It is a maven project, meaning all the dependencies are in the pom.xml file, allowing anyone to easily clone the project on their local. It is a Page Object model framework, using Page Factory to make it simpler and easier to maintain. (Page factory instantiates an instance of the given class and sets a lazy proxy for each of the WebElement and List fields that have been declared).

Dependencies:
•	Java

•	Maven

Technologies and tools:

•	Selenium - to drive the web browser

•	Java 11 (higher version might work, but it hasn’t been tested)

•	Junit - tool used to run tests locally

•	Cucumber - runs the steps of the Gherkin scenarios

•	Cluecumber reporting plugin - a plugin to generate a comprehensive visual HTML test report

•	RestAssured (for API tests)

•	IntelliJ (IDE).

The project is structured in 3 main parts:

•	Page Objects (one java class for each screen of the application containing the declared elements and actions / methods);

•	Step Definitions (one java class for each screen of the application containing the Given / When / Then steps);

•	Feature files (one feature file for each screen of the application containing the actual gherkin scenarios).

# Setup and execution

## Setup

1.	Make sure you have installed Java SE 11 and Maven on your machine
	
2.	Clone the repository
	
3.	Open your IDE (IntelliJ)
	
4.	Open the project using pom.xml file (IntelliJ)
	
5.	Wait until all dependencies are downloaded and installed

6.	Install ‘Cucumber for Java’ and ‘Gherkin’ plugins for IntelliJ to view the feature files better and be able to navigate to the step definition from the feature file.

## Execution

In order to run tests from the Command Line, you need to download Maven and set it up properly. 
The command to run tests from the Command Line (Terminal) is: `mvn clean verify` or `mvn clean install`
You will have to pass the various arguments as :

`mvn clean verify -Dcucumber.options="--tags @SmokeTest" -Denvironment=Test`
(Read more about tags below)

## Feature tagging

By default, all tests from features folder is run. When running tests from the command line it's possible to override this setting by using the cucumber.options java system property.
For example, to runs only scenarios or features tagged with @SmokeTest, the command would be :

`mvn clean verify -Dcucumber.options="--tags @SmokeTest" -Denvironment=Test`

The tags used for each feature can be found above individual test scenarios in their feature files.

## Parallel execution

To speed up the execution of the tests, they can be run in parallel with the approach described here Using the surefire plugin.
The settings are in the pom.xml in the build profile configuration section:
It is currently set to run a total of 3 threads across all cores (i.e 3 at a time)
If the thread count is greater than one, each feature file is run in parallel, but individual scenarios within a feature are run serially.
After much experimenting it is set to 3 to make sure we don’t have flaky tests during execution.

## Reporting

This framework uses Cluecumber reporting plugin to create a html file with the test results in target\test-report folder, uses Selenium’s TakesScreenshot utility to capture a full screenshot on error and also adds the browser logging for any failed test into the hooks section of the report.
The link to the online report will be generated and output in the console logs at the end of the execution. 


# Mac OS X Setup instructions

* Download and install Java
* From the command line run `brew install maven`
* Follow the execution instructions above to run the tests



