package com.rioliu.test.selenium;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.rioliu.test.logging.Logger;
import com.rioliu.test.logging.LoggerFactory;


/**
 * Created by rioliu on Nov 16, 2018
 */
public abstract class BasePageObject {

    private Logger logger = LoggerFactory.getConsoleLogger(BasePageObject.class);

    protected final WebDriver driver;

    public BasePageObject(WebDriver driver) {
        this.driver = driver;
    }

    public void clearAndSetValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    public void clearAndType(By locator, String value) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    public void clickWithTimeout(By locator) throws TimeoutException {
        clickWithTimeout(locator, getSeleniumElementWaitTimeout(), TimeUnit.SECONDS);
    }

    public void clickWithTimeout(By locator, long time, TimeUnit unit) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, unit.toSeconds(time));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public WebElement findElementByAll(By... all) {
        return driver.findElement(new ByAll(all));
    }

    public WebElement findElementByIdOrName(String idOrName) {
        return driver.findElement(new ByIdOrName(idOrName));
    }

    public WebElement findElementByJQuery(String selector) {
        return driver.findElement(new ByJQuery(selector));
    }

    public List<WebElement> findElementsByIdOrName(String idOrName) {
        return driver.findElements(new ByIdOrName(idOrName));
    }

    public List<WebElement> findElementsByJQuery(String selector) {
        return driver.findElements(new ByJQuery(selector));
    }

    private int getSeleniumElementWaitTimeout() {

        String timeout = System.getProperty("selenium.element.wait.timeoutInSecs", "60");
        if (NumberUtils.isDigits(timeout) == false) {
            timeout = "60";
        }

        return NumberUtils.toInt(timeout);

    }

    public boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentAndDisplay(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isTextPresent(String text) {
        return driver.getPageSource().contains(text);
    }

    public boolean isTextPresentWithTimeout(By locator, String text) throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, getSeleniumElementWaitTimeout());
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public void maxmize() {
        if (driver != null) {
            driver.manage().window().maximize();
        }
    }

    public void navigate(String url) {
        driver.get(url);
    }

    public void pressEnterKey(By locator) {
        WebElement element = driver.findElement(locator);
        element.sendKeys(Keys.ENTER);
    }

    public void selectByVisibleTest(By locator, String visibleText) {
        if (driver.findElement(locator) == null || !driver.findElement(locator).isDisplayed()
                || !driver.findElement(locator).isEnabled()) {
            return;
        }
        Select selectbox = new Select(driver.findElement(locator));
        selectbox.selectByVisibleText(visibleText);
    }

    public void setInputFileValue(By locator, String value) {
        WebElement element = driver.findElement(locator);
        element.sendKeys(value);
    }

    public void sleep(long time, ChronoUnit unit) {
        try {
            Sleeper.SYSTEM_SLEEPER.sleep(Duration.of(time, unit));
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void takeScreenshot(String fileName) {

    }

}
