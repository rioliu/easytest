# easytest

This project is a quick start framework to develop automation test project. especially for REST and Selenium Tests.

- com.rioliu.test.config.TestContext.java

This class can help you to load test properties from Env/System and Property files. 

e.g. 

        TestContext tc = TestContext.get();
        try {
            PropertiesConfiguration pc =
                    tc.loadPropertiesFromFile(
                            "src/test/resources/test.properties");

            System.out.println(pc.getString("version"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
 
 - com.rio.liu.test.base.AbstractTestBase.java
 
 This is a testNG base class, you can develop your test case base on it. it is inherited from org.testng.TestListenerAdapter, help you to do logging for test events like start/stop/skip/failure. Also it can help to initialize Selenium WebDrivers (firefox/chrome) with default/customized Options. you don't need to care about the WebDriver destory, all the drivers will be closed/quit when the test is finished, and when the WebUI test is failed, screenshot will be taken automatically.
 
        WebDriver firefox = firefox();
        WebDriver chrome = chrome();

        firefox.get("https://www.whatsmybrowser.org/");
        chrome.get("https://www.whatsmybrowser.org/");

        takeScreenshot(firefox, "home_page_ff.png", "open home page");
        takeScreenshot(chrome, "home_page_chrome.png", "open home page");
 
 - com.rioliu.test.selenium
 
 this package contains few Selenium Utitlites. 

  ~ BasePageObject : if you develop your WebUI test with POM model, you can use this base class to create your PageObject, it provides some basic methods to implement your test case quickly
 
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
    
- com.rioliu.test.logging

this package provides advanced logging functionalities. Common logging requirement in test is: loging in 'Console', 'Report', 'Both' 

All the loggers implemeented same Logger interface, you can use LoggerFactory.java to create ConsoleLogger and CompositeLogger (logging message in both Report and Console)

        private static Logger logger = LoggerFactory.getCompositeLogger(RESTAPITest.class);
        
        logger.info("Hello World");

- com.rioliu.test.reporting

this package provides a way to integrate your test with popular Reporting Framework ExtentReports. it can proivde a beautiful web based report.

 ![alt text](https://user-images.githubusercontent.com/3387962/48819571-93c1f880-ed8b-11e8-9e4c-3233f9c11b82.jpg)

- com.rioliu.test.rest

this package has a helper class to help you do some configuration for RestAssured Framework.

        RestClientConfigHelper.get().useRelaxedHTTPSValidation("TLSv1.2");
        
- Verify Simple REST API TEST

        given().get("https://api.publicapis.org/entries?category=animals&https=true").then()
                .body("entries.API", hasItem("Cats"));