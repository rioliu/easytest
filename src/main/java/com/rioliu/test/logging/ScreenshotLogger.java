package com.rioliu.test.logging;

/**
 * Created by rioliu on Nov 19, 2018
 */
public interface ScreenshotLogger {

    public void error(String message, String imagePath);

    public void info(String message, String imagePath);
}
