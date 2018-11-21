package com.rioliu.test.logging;

import com.rioliu.test.reporting.ExtentReportManager;

/**
 * Created by rioliu on Nov 13, 2018
 */
public class CompositeLogger implements Logger {
    
    private ConsoleLogger consoleLogger;

    public CompositeLogger(Class<?> clazz) {
        consoleLogger = new ConsoleLogger(clazz);
    }

    @Override
    public void debug(String message) {
        consoleLogger.debug(message);
        getReportLogger().debug(message);
    }

    @Override
    public void error(String message) {
        consoleLogger.error(message);
        getReportLogger().error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        consoleLogger.error(message, t);
        getReportLogger().error(message, t);
    }

    public ConsoleLogger getConsoleLogger() {
        return consoleLogger;
    }

    public ReportLogger getReportLogger() {
        return ExtentReportManager.getLogger();
    }

    @Override
    public void info(String message) {
        consoleLogger.info(message);
        getReportLogger().info(message);
    }


}
