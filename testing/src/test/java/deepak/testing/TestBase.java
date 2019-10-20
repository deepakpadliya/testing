package deepak.testing;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.internal.AbstractParallelWorker.Arguments;

import com.aventstack.extentreports.ExtentTest;

import io.github.bonigarcia.wdm.WebDriverManager;

@ContextConfiguration(classes = SpringConfig.class)
public abstract class TestBase extends AbstractTestNGSpringContextTests
{
	private ExtentTest currentTest;

	public TestBase()
	{
		// TODO Auto-generated constructor stub
	}

	public void setCurrentTest(ExtentTest currentTest)
	{
		this.currentTest = currentTest;
	}

	public ExtentTest getCurrentTest()
	{
		return currentTest;
	}

	public WebDriver OpenBrowser(String browser)
	{
		if(browser.equals("chrome"))
		{
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			return new ChromeDriver(options);
		}
		else if(browser.equals("firefox"))
		{
			WebDriverManager.firefoxdriver().version("0.26.0").setup();
			FirefoxOptions options= new FirefoxOptions();
			return new FirefoxDriver(options);
		}
		else if(browser.equals("ie"))
		{
			WebDriverManager.iedriver().version("3.14.0").setup();
			return new InternetExplorerDriver();
		}
		else
			return null;
	}

	public void closeBrowser(WebDriver driver)
	{
		if(driver != null)
		{
			try
			{
				driver.quit();
				driver.close();
			} catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public abstract List<WebDriver> getDriverList();
}
