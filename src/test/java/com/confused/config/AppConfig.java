package com.confused.config;

import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    static Properties appConfig;
    public static String browser;
    public static String device;

    public static void loadConfig() {
        if (device != null) { //Already loaded, need not load again.
            return;
        }
        appConfig = loadConfigProperties("AppConfig.properties");
        device = appConfig.getProperty("Device");
    }

    public static Properties loadConfigProperties(String filename) {
        String configLocation = System.getProperty("user.dir") + File.separator + "src/config";
        Properties props = new Properties();
        try {
            props.load(Files.newInputStream(Paths.get(configLocation + File.separator + filename)));
        } catch (IOException e) {
            Assert.fail("Exception while trying to find file " + filename + "  at location " + configLocation + ". Exception: " + e);
        }
        return props;
    }

    public static String getDevice() {
        if (device == null) {
            loadConfig();
        }
        return device;
    }

    public static String getBrowser() {
        return browser;
    }
}