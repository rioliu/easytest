package com.rioliu.test;


import java.time.temporal.ChronoUnit;

import org.hamcrest.MatcherAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.rioliu.test.base.AbstractTestBase;
import com.rioliu.test.selenium.BasePageObject;

/**
 * Created by rioliu on Nov 21, 2018
 */
public class WebDriverTest extends AbstractTestBase {


    public class GoogleHomePage extends BasePageObject {

        private String homePageUrl;

        public GoogleHomePage(WebDriver driver, String url) {
            super(driver);
            homePageUrl = url;
        }

        public void search(String keyword) {

            navigate(homePageUrl);

            clearAndType(By.name("q"), keyword);
            pressEnterKey(By.name("q"));

            sleep(5, ChronoUnit.SECONDS);

        }

        public boolean verifyResultPageContains(String expectedString) {
            return driver.getPageSource().contains(expectedString);
        }

    }

    @BeforeTest
    public void beforeTest() {

        // initialized webdriver properties
        System.setProperty("webdriver.chrome.driver",
                "/Users/rioliu/Dev/apps/webdriver/chromedriver");

        System.setProperty("webdriver.gecko.driver",
                "/Users/rioliu/Dev/apps/webdriver/geckodriver");
    }

    @Test
    public void testPageObject() {

        WebDriver driver = chrome();

        GoogleHomePage home = new GoogleHomePage(driver, "https://www.google.com/ncr");
        home.search("Google");

        takeScreenshot(driver, "google_search_result.png", "Search result on Google");

        MatcherAssert.assertThat("can find google home page url on search page",
                home.verifyResultPageContains("www.google.com"));
    }


    @Test(enabled = false)
    public void testWebDriver() {

        WebDriver firefox = firefox();
        WebDriver chrome = chrome();

        firefox.get("https://www.whatsmybrowser.org/");
        chrome.get("https://www.whatsmybrowser.org/");

        takeScreenshot(firefox, "home_page_ff.png", "open home page");
        takeScreenshot(chrome, "home_page_chrome.png", "open home page");

    }

}
