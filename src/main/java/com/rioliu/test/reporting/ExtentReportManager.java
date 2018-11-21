package com.rioliu.test.reporting;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.rioliu.test.logging.ReportLogger;


/**
 * Created by rioliu on Oct 25, 2018
 */
public class ExtentReportManager {

    private static ExtentReports extent;

    private static ExtentTest test;
    public static String DFAULT_FILE_PATH = "test-output/automation_test_report.html";
    private static ConcurrentHashMap<Long, ExtentTest> loggers =
            new ConcurrentHashMap<Long, ExtentTest>();
    private static ConcurrentHashMap<String, ExtentTest> parentLoggers =
            new ConcurrentHashMap<String, ExtentTest>();
    public synchronized static ExtentTest createNode(String parentTestName,
            String testName) {
        
        ExtentTest parent = parentLoggers.get(parentTestName);
        ExtentTest child = parent.createNode(testName);
        loggers.put(Thread.currentThread().getId(), child);

        return child;
    }

    public static ReportLogger getLogger() {
        return new ReportLogger(loggers.get(Thread.currentThread().getId()));
    }

    public synchronized static ExtentTest getParentLogger(String testName) {
        return parentLoggers.get(testName);
    }

    public synchronized static ExtentReports getReporter() {
        return getReporter(null);
    }

    public synchronized static ExtentReports getReporter(String filePath) {

        if (extent == null) {

            if (StringUtils.isEmpty(filePath)) {
                filePath = DFAULT_FILE_PATH;
            }


            ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
            htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
            htmlReporter.config().setChartVisibilityOnOpen(true);
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setEncoding("utf-8");

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);

            for (String name : System.getProperties().stringPropertyNames()) {
                extent.setSystemInfo(name, System.getProperty(name));
            }

        }

        return extent;

    }

    public synchronized static ExtentTest startTest(ExtentReports reporter,
            String testName,
            String description) {

        if (StringUtils.isEmpty(testName)) {
            reporter = getReporter(null);
        }

        test = StringUtils.isNotEmpty(description) ? reporter.createTest(testName, description)
                : reporter.createTest(testName);

        parentLoggers.put(testName, test);

        return test;
    }

    private ExtentReportManager() {}




}
