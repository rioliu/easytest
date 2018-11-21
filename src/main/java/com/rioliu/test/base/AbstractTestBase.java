package com.rioliu.test.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.rioliu.test.logging.CompositeLogger;
import com.rioliu.test.logging.LoggerFactory;
import com.rioliu.test.logging.ScreenshotLogger;
import com.rioliu.test.selenium.Utils;

/**
 * Created by rioliu on Nov 16, 2018
 */
public class AbstractTestBase extends TestListenerAdapter {

    private static final CompositeLogger logger =
            (CompositeLogger) LoggerFactory.getCompositeLogger(AbstractTestBase.class);


    public static final String ATTR_DRIVER = "WebDrivers";

    protected String outputDir;
    protected FirefoxOptions defaultFirefoxOptions;
    protected ChromeOptions defaultChromeOptions;
    private ArrayList<WebDriver> unusedDrivers = new ArrayList<WebDriver>();

    public AbstractTestBase() {}


    protected WebDriver chrome() {

        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars");

        WebDriver driver =
                defaultChromeOptions != null ? chrome(defaultChromeOptions)
                        : new ChromeDriver(options);
        pushDriverToStack(driver);

        return driver;
    }


    protected WebDriver chrome(ChromeOptions options) {
        WebDriver driver = new ChromeDriver(options);
        pushDriverToStack(driver);

        return driver;
    }


    protected WebDriver firefox() {

        WebDriver driver = defaultFirefoxOptions != null ? firefox(defaultFirefoxOptions)
                : new FirefoxDriver();
        pushDriverToStack(driver);

        return driver;
    }

    protected WebDriver firefox(FirefoxOptions options) {
        WebDriver driver = new FirefoxDriver(options);
        pushDriverToStack(driver);

        return driver;
    }


    /**
     * create wrapper webdriver for multi-threaded usage
     *
     * @param driver
     */
    protected WebDriver getThreadSafeWebDriver(WebDriver driver) {
        return ThreadGuard.protect(driver);
    }


    @Override
    public void onFinish(ITestContext testContext) {
        for (WebDriver driver : unusedDrivers) {
            driver.close();
            driver.quit();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onTestFailure(ITestResult tr) {

        super.onTestFailure(tr);

        logger.getConsoleLogger().error("TEST " + tr.getMethod().getMethodName() + " is FAILED",
                tr.getThrowable());;

        // take screenshot and add the file to report
        ArrayList<WebDriver> list = (ArrayList<WebDriver>) tr.getAttribute(ATTR_DRIVER);
        if (list != null) {
            for (WebDriver driver : list) {
                takeScreenshot(driver, null, null, true);
                unusedDrivers.add(driver);
            }
        }

        tr.removeAttribute(ATTR_DRIVER);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        logger.getConsoleLogger().info("TEST " + tr.getMethod().getMethodName() + " is SKIPPED");
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.getConsoleLogger()
                .info("TEST " + result.getMethod().getMethodName() + " is STARTING");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onTestSuccess(ITestResult tr) {
        
        logger.getConsoleLogger().info("TEST " + tr.getMethod().getMethodName() + " is SUCCESS");

        ArrayList<WebDriver> stack = (ArrayList<WebDriver>) tr.getAttribute(ATTR_DRIVER);
        if (stack != null) {
            for (WebDriver driver : stack) {
                unusedDrivers.add(driver);
            }
        }
        tr.removeAttribute(ATTR_DRIVER);
    }


    @SuppressWarnings("unchecked")
    private void pushDriverToStack(WebDriver driver) {
        ITestResult result = Reporter.getCurrentTestResult();
        Object drivers = result.getAttribute(ATTR_DRIVER);
        if (drivers == null) {
            ArrayList<WebDriver> list = new ArrayList<>();
            list.add(driver);
            result.setAttribute(ATTR_DRIVER, list);
        } else {
            ((ArrayList<WebDriver>) drivers).add(driver);
        }
    }

    protected void setDefaultChromeOptions(ChromeOptions options) {
        this.defaultChromeOptions = options;
    }

    protected void setDefaultFirefoxOptions(FirefoxOptions options) {
        this.defaultFirefoxOptions = options;
    }

    protected void takeScreenshot(WebDriver driver, String fileName, String description) {
        takeScreenshot(driver, fileName, description, false);
    }

    protected void takeScreenshot(WebDriver driver, String fileName, String description,
            boolean isError) {

        ITestResult result = Reporter.getCurrentTestResult();
        outputDir = new StringBuilder(result.getTestContext().getOutputDirectory())
                        .append(File.separator)
                        .append(result.getMethod().getMethodName()).toString();

        String imagePath = Utils.takeScreenshot(driver, outputDir, fileName);
        ScreenshotLogger screenshotLogger = logger.getReportLogger();
        if (isError) {
            screenshotLogger.error(description, imagePath);
        } else {
            screenshotLogger.info(description, imagePath);
        }
    }

}
