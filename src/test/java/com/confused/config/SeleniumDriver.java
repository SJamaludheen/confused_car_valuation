package com.confused.config;

import com.confused.util.Debugger;
import cucumber.api.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import com.confused.lib.SeleniumLib;
import java.util.concurrent.TimeUnit;


public class SeleniumDriver extends EventFiringWebDriver {

    public static WebDriver DRIVER;
    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            try {
                DRIVER.quit();
            } catch (Exception exp) {
               // Debugger.println("Exception while quiting the driver : " + exp.getLocalizedMessage());
            }
        }
    };

    static {
        DRIVER = new BrowserFactory().getDriver();
        // SeleniumLib.clearAllSnapShots();
        SeleniumLib.ParentWindowID = DRIVER.getWindowHandle();
        DRIVER.manage().window().maximize();
        DRIVER.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public static void initDriver() {
        try {
            DRIVER.quit();
            DRIVER = new BrowserFactory().getDriver();
            SeleniumLib.ParentWindowID = DRIVER.getWindowHandle();
            DRIVER.manage().window().maximize();
            DRIVER.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
        } catch (Exception exp) {
            //Debugger.println("Exception while trying to quit the driver. " + exp.getLocalizedMessage());
        }
    }

    public SeleniumDriver() {
        super(DRIVER);
    }

    @Override
    public void close() {
        if (Thread.currentThread() != CLOSE_THREAD) {
            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
        }
        super.close();
    }

    @Before
    /**
     * Delete all cookies at the start of each scenario to avoid
     * shared state between tests
     */
    public void deleteAllCookies() {
        manage().deleteAllCookies();
    }
}