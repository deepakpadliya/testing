package deepak.testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
	
	public WebDriver driver;
	
	public WebDriverWait wait;
	
	public void init(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		wait = new WebDriverWait(driver, 30);		
	}
}
