package deepak.testing.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestUtils {
	public static String captureScreenshot(WebDriver driver) throws IOException{
		TakesScreenshot screenshot= (TakesScreenshot) driver;
		File src = screenshot.getScreenshotAs(OutputType.FILE);
		File dest = new File("./test-output/"+new Date().getTime()+".jpeg");

		FileUtils.moveFile(src,dest);
		return dest.getAbsolutePath();
	}
	
	
	
}
