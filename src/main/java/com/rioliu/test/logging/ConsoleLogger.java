package com.rioliu.test.logging;

import org.slf4j.LoggerFactory;

/**
 * Created by rioliu on Nov 13, 2018
 */
public class ConsoleLogger implements com.rioliu.test.logging.Logger {


    private org.slf4j.Logger logger;

    /*
     * https://tinylog.org/configuration
     */
    public ConsoleLogger(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public void debug(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

}
