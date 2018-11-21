package com.rioliu.test.selenium;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.rioliu.test.logging.Logger;
import com.rioliu.test.logging.LoggerFactory;


/**
 * Created by rioliu on Nov 16, 2018
 */
public class Utils {

    private static final Logger logger = LoggerFactory.getConsoleLogger(Utils.class);

    public static String takeScreenshot(WebDriver driver, String outputDir, String fileName) {

        if (driver == null) {
            throw new IllegalArgumentException("WebDriver object is null");
        }

        if (StringUtils.isEmpty(outputDir)) {
            outputDir = System.getProperty("user.home");
        } else {
            File dir = new File(outputDir);
            if (!dir.exists()) {
                try {
                    FileUtils.forceMkdir(dir);
                } catch (IOException e) {
                    logger.error("create dir " + outputDir + " failed", e);
                }
            }

            if (!dir.isDirectory()) {
                throw new IllegalArgumentException(outputDir + " is not a directory");
            }
        }

        if (StringUtils.isEmpty(fileName)) {
            fileName = "screenshot_" + String.valueOf(System.currentTimeMillis()) + ".png";
        }

        TakesScreenshot ts = (TakesScreenshot) driver;
        File srcFile = ts.getScreenshotAs(OutputType.FILE);
        File destFile = new File(outputDir, fileName);

        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            logger.error("copy screenshot from " + srcFile.getAbsolutePath() + " to "
                    + destFile.getAbsolutePath() + " failed", e);
        }

        return destFile.getAbsolutePath();

    }

    private Utils() {}

}
