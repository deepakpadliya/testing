package deepak.listeners;

import java.io.File;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import deepak.testing.TestBase;
import deepak.testing.utils.TestUtils;

public class GenerateReport implements ITestListener {
	
	private ExtentHtmlReporter extentHtmlReporter;
	private ExtentReports extentReport;
	
	private ExtentTest test;

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("Test started");
		Object params[] = result.getParameters();
		if(params[0] instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,Object> testData = (Map<String, Object>) params[0];
			test= extentReport.createTest(Feature.class,testData.get("testDescription").toString());
		}
		else {
			test=extentReport.createTest(Feature.class,result.getInstanceName());
		}
		TestBase tb = (TestBase)result.getInstance();
		tb.setCurrentTest(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		setStatus(result);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		takeScreenshot(result);
		setStatus(result);
	}

	private void takeScreenshot(ITestResult result) {
		TestBase testbase = (TestBase) result.getInstance();
		testbase.getDriverList().forEach(d->{
			try {
				test.addScreenCaptureFromPath(TestUtils.captureScreenshot(d));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				test.log(Status.WARNING, "Not able to take screenshot");
			}
		});
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		takeScreenshot(result);
		test.pass("Test case skipped");
		extentReport.flush();		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}
	
	public void setStatus(ITestResult result) {
		Object params[]= result.getParameters();
		if(params[0] instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String,Object> testData = (Map<String, Object>) params[0];
			if(testData.get("expected").equals("fail") && test.getStatus()==Status.PASS) {
				test.fail("Expected fail but found pass");
				result.setStatus(ITestResult.FAILURE);
			}
			else if(testData.get("expected").equals("pass") && test.getStatus()==Status.FAIL) {
				test.fail("Expected pass but found fail");
				result.setStatus(ITestResult.FAILURE);
			}
			else {
				test.pass("Test case pass");
			}
		}
		else if(Status.PASS==test.getStatus()) {
			test.pass("Test case pass");
			result.setStatus(ITestResult.SUCCESS);
		}
		else {
			result.setStatus(ITestResult.FAILURE);
			test.fail("Test case fail");
		}
		extentReport.flush();
	}
	
	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		File outputDirectory = new File(context.getOutputDirectory());
		if (!outputDirectory.exists()) {
			outputDirectory.mkdirs();
		}
		File f = new File(context.getOutputDirectory() + context.getCurrentXmlTest().getName()+".html");
		extentHtmlReporter = new ExtentHtmlReporter(f);
		extentReport = new ExtentReports();
		extentReport.attachReporter(extentHtmlReporter);
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}
