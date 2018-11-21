package com.rioliu.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.rioliu.test.base.AbstractTestBase;
import com.rioliu.test.logging.Logger;
import com.rioliu.test.logging.LoggerFactory;
import com.rioliu.test.rest.RestClientConfigHelper;

public class RESTAPITest extends AbstractTestBase {
    
    private static Logger logger = LoggerFactory.getCompositeLogger(RESTAPITest.class);

    
    @BeforeClass
    public void beforeClass() {
        RestClientConfigHelper.get().useRelaxedHTTPSValidation("TLSv1.2");
    }

    @Test
    public void testPublicAPI1() {
        
        logger.info("Test REST API with parameters category and https");

        given().get("https://api.publicapis.org/entries?category=animals&https=true").then()
                .body("entries.API", hasItem("Cats"));

        logger.debug("Expected result found");

    }

    @Test
    public void testPublicAPI2() {
        
        logger.info("Test REST API with parameters category");

        given().get("https://api.publicapis.org/categories").then()
                .body(".", hasItems("Weather", "Video"));
    }
}
