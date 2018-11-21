package com.rioliu.test.logging;

/**
 * Created by rioliu on Nov 13, 2018
 */
public class LoggerFactory {

    public static Logger getCompositeLogger(Class<?> clazz) {
        return new CompositeLogger(clazz);
    }

    public static Logger getConsoleLogger(Class<?> clazz) {
        return new ConsoleLogger(clazz);
    }

    private LoggerFactory() {}


}
