package deepak.testing;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import deepak.dataprovider.SpreadsheetTestDataProvider;
import deepak.listeners.GenerateReport;
import deepak.testing.utils.TestUtils;

/**
 * Unit test for simple App.
 */
@Listeners(GenerateReport.class)
public class AppTest extends TestBase
{
	WebDriver driver = null;

	@Autowired
	App app;

	HashMap<String, String> param;

	@BeforeTest()
	public void beforeTest(ITestContext context)
	{
		param = (HashMap<String, String>) context.getCurrentXmlTest().getAllParameters();
	}

	@Test(dataProvider = "getData", dataProviderClass = SpreadsheetTestDataProvider.class)
	public void AppTestcase(Map<String, Object> testData) throws IOException
	{
			ExtentTest test = getCurrentTest();
			driver = OpenBrowser(param.get("browser"));
			List<String> searchEngines = (List<String>) testData.get("searchEngine");
			for(int j = 0; j < searchEngines.size(); j++)
			{
				ExtentTest scNode = null;
				try
				{
					driver.get(searchEngines.get(j));

					scNode = test.createNode(searchEngines.get(j));
					app.init(driver);
					scNode.log(Status.INFO, "ready to search", MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.captureScreenshot(driver)).build());
					scNode.log(Status.INFO, testData.get("search").toString());
					List<WebElement> results = app.search(testData.get("search").toString());
					scNode.log(Status.INFO, "search result ", MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.captureScreenshot(driver)).build());

					if(results.size() != 0)
						scNode.log(Status.INFO, "Result " + results.size()).pass("pass");
					else
						scNode.log(Status.INFO, "result").fail("fail");
				} catch(Exception ex)
				{
					if(scNode != null)
					{
						scNode.log(Status.FAIL, "", MediaEntityBuilder.createScreenCaptureFromPath(TestUtils.captureScreenshot(driver)).build());
						scNode.fail(ex);
					}else {
						test.fail(ex);
					}
				}
		}

	}

	@AfterMethod
	public void tearDown()
	{
		if(driver != null)
		{
			try
			{
				driver.quit();
				driver.close();
			} catch(Exception ex)
			{
			}
		}
	}

	@Override
	public List<WebDriver> getDriverList()
	{
		// TODO Auto-generated method stub
		return Arrays.asList(driver);
	}

}
