package com.rioliu.test.logging;

import java.io.IOException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

/**
 * Created by rioliu on Nov 14, 2018
 */
public class ReportLogger implements Logger, ScreenshotLogger {

    private ExtentTest logger;

    public ReportLogger(ExtentTest test) {
        logger = test;
    }


    @Override
    public void debug(String message) {
        logger.debug(message);
    }


    @Override
    public void error(String message) {
        logger.error(message);
    }


    @Override
    public void error(String message, String imagePath) {
        try {
            logger.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void error(String message, Throwable t) {
        logger.error(message);
        logger.error(t);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(String message, String imagePath) {
        try {
            logger.info(message, MediaEntityBuilder.createScreenCaptureFromPath(imagePath).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
