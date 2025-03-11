package com.confused.config;

import org.junit.Assert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static com.confused.inputdata.ActiveEnvironment.setCurrentBrowser;

public class BrowserConfig {
    static Properties config;
    public static String browser;

    public static void loadConfig() {
        if (browser != null) {  //Already loaded, need not load again.
            return;
        }
        config  = loadConfigProperties("BrowserConfig.properties");
        browser = config.getProperty("Browser");
    }

    public static Properties loadConfigProperties(String filename) {
        String configLocation = System.getProperty("user.dir") + File.separator + "src/config";
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(configLocation + File.separator + filename));
        } catch (IOException e) {
            Assert.fail("File: " + filename + " not present in location : " + configLocation + ". Exception: " + e);
        }
        return prop;
    }

    public static String getBrowser() {
        browser = System.getProperty("selected_browser");
        setCurrentBrowser((browser == null || browser.isEmpty()) ? "FROM_CONFIG" : browser);
        if (browser == null || browser.isEmpty() || browser.equalsIgnoreCase("FROM_CONFIG")) {
            //browser = null;//To ensure that it reads from the config file
            loadConfig();
        }
        return browser;
    }
}