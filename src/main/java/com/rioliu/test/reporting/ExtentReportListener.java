package com.rioliu.test.reporting;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

/**
 * Created by rioliu on Oct 25, 2018
 */
public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;

    private static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
    public ExtentReportListener() {}

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    @Override
    public void onStart(ITestContext context) {
        extent = ExtentReportManager
                .getReporter(context.getOutputDirectory() + "/automation_test_report.html");
        ExtentReportManager.startTest(extent, context.getName(), "");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().error(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip(result.getThrowable());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest child = ExtentReportManager.createNode(result.getTestContext().getName(),
                result.getMethod().getMethodName());
        test.set(child);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

}
