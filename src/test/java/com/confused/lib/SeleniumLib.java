package com.confused.lib;

import com.confused.util.Debugger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.List;

public class SeleniumLib {
    private static WebDriver driver;
    private static final boolean HIGHLIGHT = true;
    private static WebElement webElement = null;
    private String strtext;
    public static String ParentWindowID = null;

    public SeleniumLib(WebDriver driver) {
        SeleniumLib.driver = driver;
    }

    private static String timeoutErrorMessage(By element) {
        return "Unable to find visibility of element located by " + element + " within " + "10" + " seconds \n "
                + "\nThere several reasons why this may have occurred including: \n" +
                "\n - The page did not finish loading some ui features before the expected timeout (usually related to Ajaxian elements)" +
                "\n - The ui element is legitimately missing from the page due to a bug or feature change " +
                "\n - There was a problem with communication within the Selenium stack" +
                "\nSee image below (if available) for clues.\n If all else fails, RERUN THE TEST MANUALLY! - CPK\n\n";
    }

    public static WebElement waitForElementVisible(By element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        final WebElement el = driver.findElement(element);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return el.isDisplayed();
            }
        });
        return el;
    }

    /**
     * @param element
     * @return
     */
    private WebElement getElement(By element) {
        try {
            webElement = waitForElementVisible(element);
            return webElement;
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + " Not Found ");
            return null;
        }
    }

    /**
     * @param element
     */
    public static void elementHighlight(WebElement element) {
        if (HIGHLIGHT) {
            for (int i = 0; i < 10; i++) {
                JavascriptExecutor javascript = (JavascriptExecutor) driver;
                javascript.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                        "color: pink; border: 3px solid red;");
                javascript.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
            }
        }
    }

    /**
     * @param element
     */
    public void clickOnElement(By element) {
        try {
            WebElement webele = getElement(element);
            if (webele != null) {
                webele.click();
            } else {
                Actions actions = new Actions(driver);
                actions.moveToElement(driver.findElement(element)).click().perform();
            }
        } catch (Exception exp) {
            try {
                Actions actions = new Actions(driver);
                actions.moveToElement(driver.findElement(element)).click().perform();
            } catch (Exception exp1) {
                Debugger.println("Exception : SeleniumLib.clickOnElement: " + element.toString() + "\n" + exp1);
                throw exp1;
            }
        }
    }

    public List<WebElement> getElements(By ele) {
        try {
            waitForElementVisible(driver.findElement(ele));
            return driver.findElements(ele);
        } catch (NoSuchElementException exp) {
            return null;
        }
    }

    public void clearValue(By element) {
        try {
            webElement = getWebElement(element);
            webElement.clear();
        } catch (NoSuchElementException e) {
        }
    }

    public void sendValue(By element, String value) {
        try {
            webElement = getWebElement(element);
            webElement.clear();
            webElement.sendKeys(value);
        } catch (NoSuchElementException J) {
            throw new NoSuchElementException(timeoutErrorMessage(element) + J);
        }
    }

    public void selectFromListByText(By element, String text) {
        try {
            webElement = getElement(element);
            elementHighlight(webElement);
            new Select(getElement(element)).selectByVisibleText(text);

        } catch (NoSuchElementException e) {
            Debugger.println("element not found  " + element);
            boolean throwExp = true;
            try {
                Select select = new Select(webElement);
                List<WebElement> options = select.getOptions();
                for (WebElement option : options) {
                    String originalText = text.trim().replace(" ", "").replace(" ", "").toLowerCase();
                    String expectedString = option.getText().trim().replace(" ", "").toLowerCase();
                    if (originalText.equalsIgnoreCase(expectedString)) {
                        select.selectByVisibleText(option.getText());
                        return;
                    }
                }
            } catch (Exception exp) {
                throw exp;
            }
        }
    }

    public static void selectFromListByValue(By element, String text) {
        try {
            webElement = getWebElement(element);
            Select select = new Select(webElement);
            select.selectByValue(text);
        } catch (NoSuchElementException e) {
            Debugger.println("element not found  " + element);
            throw new NoSuchElementException(timeoutErrorMessage(element) + e);
        }
    }

    public static void refreshPage() {
        driver.navigate().refresh();
    }

    public static boolean isDisplayed(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            return webElement.isDisplayed();
        } catch (Exception exp) {
            Debugger.println("Element " + element + " not displayed. Exception : " + exp);
            return false;
        }
    }

    public boolean isElementPresent(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            return webElement.isDisplayed();
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + "  not displayed");
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    public boolean isElementPresent(WebElement element) {
        try {
            try {
                FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver);
                wait.withTimeout(Duration.ofSeconds(30));
                wait.pollingEvery(Duration.ofSeconds(5));
                wait.ignoring(NoSuchElementException.class);
                wait.ignoring(StaleElementReferenceException.class);
                wait.until(ExpectedConditions.visibilityOf(element));
                return element.isDisplayed();
            } catch (Exception exp) {
                return false;
            }
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + "  NOT displayed");
            return false;
        }
    }

    public void waitForElementVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        final WebElement el = element;
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return el.isDisplayed();
            }
        });
    }

    public boolean javaScriptClick(By element) {
        try {
            webElement = getWebElement(element);
            if (webElement == null) {
                return false;
            }
            elementHighlight(webElement);
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", webElement);
            return true;
        } catch (Exception exp) {
            Debugger.println("Exception: SeleniumLib: Javascript Click.." + exp);
            return false;
        }
    }

    public static WebElement getWebElement(By by) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception exp) {
            return null;
        }
    }

    /**
     * @param element
     * @return
     */
    public String getText(By element) {
        try {
            webElement = waitForElementVisible(element);
            elementHighlight(webElement);
            strtext = webElement.getText().trim();
            return strtext;

        } catch (Exception e) {
            return "";
        }
    }

    public static void sleep(int i) {
        try {
            Thread.sleep(i * 2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepInMinutes(int i) {
        try {
            Thread.sleep((long) i * 1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepInSeconds(int i) {
        try {
            Thread.sleep(i * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void focusElement(By element) {
        webElement = getElement(element);
        JavascriptExecutor javascript = (JavascriptExecutor) driver;
        javascript.executeScript("arguments[0].focus();", webElement);
    }

    public void waitForAjax(int timeoutInSeconds) {
        //Checking active ajax calls by calling jquery.active
        try {
            if (driver instanceof JavascriptExecutor) {
                JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
                for (int i = 0; i < timeoutInSeconds; i++) {
                    Object numberOfAjaxConnections = jsDriver.executeScript("return jQuery.active");
                    // return should be a number
                    if (numberOfAjaxConnections instanceof Long) {
                        Long n = (Long) numberOfAjaxConnections;
                        if (n.longValue() == 0L)
                            break;
                    }
                    Thread.sleep(1000);
                }
            } else {
                Debugger.println("Web driver: " + driver + " cannot execute javascript");
            }
        } catch (InterruptedException e) {
            Debugger.println("Ajax wait exception : " + e);
        }
    }

    public boolean changeWindow() {
        try {
            sleepInSeconds(3);//Wait for 3 seconds before changing the window
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
            }
            return true;
        } catch (Exception exp) {
            Debugger.println("Exception while switching window: " + exp);
            return false;
        }
    }

    public static boolean closeCurrentWindow() {
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            Debugger.println("Closing Current Window: " + tabs.size());
            String title = driver.getTitle();
            if (title == null || title.isEmpty()) {
                driver.switchTo().window(tabs.get(0));
                return true;
            }
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.close()");
            }
            driver.switchTo().window(tabs.get(0));
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public static boolean removeExtraTab() {
        try {
            Debugger.println("Removing Extra Tab....");
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("window.close()");
            }
            driver.switchTo().window(tabs.get(0));
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public static boolean switchToFirstTab() {
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(0));
            }
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public static boolean switchToSecondTab() {
        try {
            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            if (tabs.size() > 1) {
                driver.switchTo().window(tabs.get(1));
            }
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public static void changeToParentWindow() {
        try {
            driver.switchTo().window(ParentWindowID);
        } catch (Exception exp) {
            Debugger.println("Exception from changing to parent window. Exception : " + exp);
        }
    }

    public void clickOnLink(String string) {
        waitForAjax(2);
        driver.findElement(By.linkText(string)).click();
        waitForAjax(4);
    }

    public static void clickLink(String text) {
        try {
            driver.findElement(By.linkText(text)).isDisplayed();
            driver.findElement(By.linkText(text)).click();
        } catch (NoSuchElementException J) {
            Debugger.println("element not found " + text);
        }
    }

    public static boolean upload(String path) {
        WebElement element;
        // Switch to newly opened window
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
        sleep(2);
        element = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("file")));
        if (element != null) {
            element.click();
        }
        sleep(2);
        //Copy file path to cllipboard
        StringSelection ss = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        //Java Robot commands to paste the clipboard copy on focused textbox
        Robot robot = null;
        try {
            robot = new Robot();

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            sleep(2);
            element = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("html/body/form/div/p/input[1]")));
            element.click();
            sleep(2);
            element = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@value='Close Window']")));
            element.click();
            sleep(2);
            return true;
        } catch (Exception exp) {
            Debugger.println("Exception while trying to upload : " + exp);
            return false;
        }
    }

    public static void Wait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isTextPresent(String text) {
        try {
            if (text == null) {
                return false;
            }
            return getVisibleText().contains(text);
        } catch (Exception exp) {
            return false;
        }
    }

    /**
     * @return
     */
    public static String getVisibleText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    public String moveAndClickOn(By element) {
        try {
            Actions action = new Actions(driver);
            WebElement we = driver.findElement(element);
            action.moveToElement(we).build().perform();
            return "Success";
        } catch (Exception exp) {
            return "Exception in clicking on Element:" + element.toString() + ",EXP:" + exp;
        }
    }

    public String getAttributeValue(By element, String attribute) {
        try {
            webElement = getElement(element);
            elementHighlight(webElement);
            return webElement.getAttribute(attribute);
        } catch (NoSuchElementException e) {
            Debugger.println("[Error] Selenium Lib....getAttributeValue..." + element.toString() + "Not found");
            return null;
        }
    }

    // Add by manjunath K M 12-9-16
    public List<WebElement> getHeadingElements(By element) {
        try {
            waitForElementVisible(driver.findElement(element));
            return driver.findElements(element);
        } catch (NoSuchElementException exp) {
            exp.printStackTrace();
            Debugger.println("SeleniumLib: [Error]" + element.toString() + " Not Found ");
            return null;
        }
    }

    public int getNoOfRows(By element) {
        try {

            waitForElementVisible(driver.findElement(element));
            return driver.findElements(element).size();
        } catch (NoSuchElementException exp) {
            Debugger.println("SeleniumLib: [Error]" + element.toString() + " Not Found ");
            return 0;
        }
    }

    public boolean isPopupDisplayed(By popby) {
        try {
            WebElement popmsg_element = driver.findElement(popby);
            return popmsg_element.isDisplayed();
        } catch (Exception exp) {
            return false;
        }
    }

    public static void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    public static void reInitializeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir") + File.separator + "drivers/chromedriver.exe");

        driver = new ChromeDriver(getChromeOptions(null, true));
    }

    static ChromeOptions getChromeOptions(String userAgent,
                                          boolean javascriptEnabled) {
        ChromeOptions opts = new ChromeOptions();
        if (null != userAgent) {
            opts.addArguments("user-agent=" + userAgent);
        }
        if (!javascriptEnabled) {
            opts.addArguments("disable-javascript");
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("chrome.switches", Collections.singletonList("--incognito"));
        opts.merge(capabilities);
        return opts;
    }

    public int getColumnIndex(By TableHeading, String column_name) {
        List<WebElement> Headings = getHeadingElements(TableHeading);
        if (Headings == null || Headings.size() == 0) {
            return -1;
        }
        String heading_name = "";
        for (int index = 0; index < Headings.size(); index++) {
            heading_name = Headings.get(index).getText();
            if (column_name.equalsIgnoreCase(heading_name)) {
                return index + 1;
            }
        }
        return -1;
    }

    public int getWindowWidth() {
        return driver.manage().window().getSize().getWidth();
    }

    public void acceptAlert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().accept();
        }
    }

    public void dismissAlert() {
        if (isAlertPresent()) {
            driver.switchTo().alert().dismiss();
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException ex) {
            return false;
        }
    }

    public void moveOnElement(By element) {
        Actions action = new Actions(driver);
        WebElement we = waitForElementVisible(element);
        action.moveToElement(we).build().perform();
    }

    public void moveMouseAndClickOnElement(By element) {
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(element);
        if (we == null) {
            return;
        }
        action.click(we).build().perform();
    }

    public void moveMouseAndClickOnWebElement(WebElement element) {
        Actions action = new Actions(driver);
        if (element == null) {
            return;
        }
        action.moveToElement(element).build().perform();
        sleepInSeconds(2);
        action.click(element).build().perform();
    }

    public void scrollToElement(WebElement element) {
        try {
            if (element == null) {
                return;
            }
            Point location = element.getLocation();
            String script = "scroll(" + location.x + "," + (location.y - 120) + ")";
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript(script);
        } catch (Exception e) {

        }
    }

    public boolean highLightWebElement(WebElement element) {
        try {
            if (element != null) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
                elementHighlight(element);
                return true;
            }
            return false;
        } catch (NoSuchElementException e) {
            Debugger.println("[Error]" + element.toString() + "  not displayed");
            return false;
        }
    }
}