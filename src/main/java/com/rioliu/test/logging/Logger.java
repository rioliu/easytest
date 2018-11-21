package com.rioliu.test.logging;

/**
 * Created by rioliu on Nov 13, 2018
 */
public interface Logger {

    public void debug(String message);

    public void error(String message);

    public void error(String message, Throwable t);

    public void info(String message);

}
