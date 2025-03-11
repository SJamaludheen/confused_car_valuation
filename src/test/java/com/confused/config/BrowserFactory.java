package com.confused.config;

import com.confused.config.AppConfig;
import com.confused.config.DeviceType;
import org.junit.Assert;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.handler.Rotate;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrowserFactory {
    WebDriver driver;

    public WebDriver getDriver() {
        return getDriver(AppConfig.getDevice(), BrowserConfig.getBrowser(), true);
    }

    public WebDriver getDriver(String device, String browser,
                               boolean javascriptEnabled) {
        DeviceType deviceType = DeviceType.valueOf(device.toUpperCase());
        BrowserEnum browserEnum = BrowserEnum.valueOf(browser.toUpperCase());
        switch (deviceType) {
            case DESKTOP:
                switch (browserEnum) {
                    case CHROME:
                        driver = getChromeDriver(null, javascriptEnabled);
                        break;
                    case FIREFOX:
                        driver = getFirefoxDriver(null, javascriptEnabled);
                        break;
                    case EDGE:
                        driver = getEdge(null, javascriptEnabled);
                        break;
                    default:
                        Assert.fail("Browser '" + browser + "' is not present in the browserEnum.");
                        break;
                }
                break;
            default:
                Assert.fail("Invalid Browser information");
        }

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        if (driver instanceof RemoteWebDriver) {
            ((RemoteWebDriver) driver).setLogLevel(Level.INFO);
        }
        if (AppConfig.device.equalsIgnoreCase("DESKTOP")) {
            driver.manage().window().maximize();
            return driver;
        }
        if (driver instanceof RemoteWebDriver && !(driver instanceof Rotate)) {
            Dimension dim = new Dimension(480, 800);
            driver.manage().window().setSize(dim);
        }
        return driver;
    }

    private WebDriver getEdge(String userAgent, boolean javascriptEnabled) {
        EdgeOptions edgeOptions = getEdgeLocalOptions(userAgent,javascriptEnabled);
        return driver = new EdgeDriver(edgeOptions);

    }

    private EdgeOptions getEdgeLocalOptions(String userAgent, boolean javascriptEnabled) {
        EdgeOptions edgeLocalOptions = new EdgeOptions();
        return edgeLocalOptions;
    }

    private WebDriver getFirefoxDriver(String userAgent,
                                       boolean javascriptEnabled) {
        return new FirefoxDriver(getFirefoxOptions(userAgent, javascriptEnabled));

    }

    private FirefoxOptions getFirefoxOptions(String userAgent,
                                             boolean javascriptEnabled) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates (true);
        profile.shouldLoadNoFocusLib();
        profile.setAssumeUntrustedCertificateIssuer(true);
        profile.setPreference("javascript.enabled", javascriptEnabled);
        String downloadFilepath = System.getProperty("user.dir") + File.separator +"downloads"+File.separator;
        try {
            File download_loc = new File(downloadFilepath);
            if (!download_loc.exists()) {
                download_loc.mkdirs();
            }
        } catch (Exception exp) {
            Assert.fail("Exception in creating download directory. Exception : " + exp);
        }
        profile.setPreference("browser.cache.disk.enable", false);
        profile.setPreference("browser.cache.memory.enable", false);
        profile.setPreference("browser.cache.offline.enable", false);
        profile.setPreference("network.http.use-cache", false);
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir",downloadFilepath);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/zip, text/html, text/tab-separated-values, text/json, text/*, text/tsv, text/csv, application/msword, application/json, application/ris, participant_id/csv, image/png, application/pdf, participant_id/html, participant_id/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        if (null != userAgent) {
            profile.setPreference("general.useragent.override", userAgent);
        }
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        // firefoxOptions.setProfile(profile);
        // firefoxOptions.setCapability("marionette", true);
        return firefoxOptions;
    }

    private WebDriver getChromeDriver(String userAgent,
                                      boolean javascriptEnabled) {
        return new ChromeDriver(getChromeOptions(userAgent, javascriptEnabled));
    }

    private ChromeOptions getChromeOptions(String userAgent,
                                           boolean javascriptEnabled) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "disable-browser-side-navigation",
                "disable-dev-shm-usage",
                "disable-gpu",
                "disable-infobars",
                "enable-automation",
                /*
                    Explicitly setting the user agent fixes a class of
                    headless XPath selector issues.
                    https://stackoverflow.com/a/69810563/479836
                */
                "user-agent='Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36'",
      //          "headless=new",
      //          "window-size=1920,1080,24",
                "ignore-ssl-errors",
                "ignore-certificate-errors",
                "no-sandbox",
                "start-maximized"
        );
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        if (null != userAgent) {
            chromeOptions.addArguments("user-agent=" + userAgent);
        }
        chromeOptions.setExperimentalOption("prefs", downloadPathSetup());
        if (!javascriptEnabled) {
            chromeOptions.addArguments("disable-javascript");
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("chrome.switches", Collections.singletonList("--incognito"));
        chromeOptions.merge(capabilities);
        return chromeOptions;
    }

    private WebDriver getRemoteFirefox(String userAgent, boolean javascriptEnabled) {
        try {
            DesiredCapabilities firefox = new DesiredCapabilities();
            firefox.setVersion("");
            firefox.setPlatform(Platform.ANY);
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefox);
            return driver;
        } catch (Exception e) {
            throw new IllegalStateException("not able to get remote firefox", e);
        }
    }
    private HashMap downloadPathSetup() {
        String downloadFilePath = System.getProperty("user.dir") + File.separator + "downloads" + File.separator;
        File location = new File(downloadFilePath);
        if (!location.exists()) {
            location.mkdirs();
        }
        HashMap<String, Object> pathPrefs = new HashMap<String, Object>();
        pathPrefs.put("profile.default_content_settings.popups", 0);
        pathPrefs.put("download.default_directory", downloadFilePath);
        return pathPrefs;
    }
}