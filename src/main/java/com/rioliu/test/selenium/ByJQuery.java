package com.rioliu.test.selenium;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Created by rioliu on Nov 16, 2018
 */
public class ByJQuery extends By implements Serializable {

    /**
     * long field serialVersionUID
     */
    private static final long serialVersionUID = -9063766046416741106L;

    private String selector;

    public ByJQuery() {}

    public ByJQuery(String selector) {

        if (StringUtils.isBlank(selector))
            throw new IllegalArgumentException("jquery expression cannot be empty");

        this.selector = selector;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return (WebElement) getJavascriptExecutor(context).executeScript(
                "return " + selector + ".get(0)");
    }

    /*
     * @see org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<WebElement> findElements(SearchContext context) {
        return (List<WebElement>) getJavascriptExecutor(context).executeScript(
                "return " + selector + ".get()");
    }

    private JavascriptExecutor getJavascriptExecutor(SearchContext context) {

        if (context instanceof JavascriptExecutor) {
            JavascriptExecutor js = (JavascriptExecutor) context;
            injectJQuery(js);

            return js;
        }

        throw new IllegalArgumentException("context is not instance of JavascriptExecutor");
    }

    private void injectJQuery(JavascriptExecutor js) {

        Boolean noJQ = (Boolean) js.executeScript("return typeof jQuery == 'undefined'");

        if (noJQ) {
            js.executeScript("var jq = document.createElement('script');"
                    + "jq.type = 'text/javascript';"
                    + "jq.src = '//code.jquery.com/jquery-2.1.0.min.js';"
                    + "document.getElementsByTagName('head')[0].appendChild(jq);");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                // ignore this exception
            }
        }

    }

    @Override
    public String toString() {
        return "By.jQuery: " + selector;
    }

}
