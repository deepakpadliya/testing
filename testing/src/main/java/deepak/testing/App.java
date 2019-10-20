package deepak.testing;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 *
 */
@Component
public class App extends BasePage
{
	@FindBy(xpath = "//input[@title='Search']| //input[@id='uh-search-box']")
	WebElement searchBox;
	
	@FindBy(xpath="//input[@aria-label='Google Search']")
	WebElement searchButton;
	
	@FindBys(@FindBy(xpath = "//div[@id='rso']/div/div/div/div/div/div/a |//li/div/div/h3/a"))
	List<WebElement> searchResults;
    
	public List<WebElement> search(String searchtext) {
		searchBox.sendKeys(searchtext+Keys.ENTER);
		wait.until(ExpectedConditions.visibilityOfAllElements(searchResults));
		return searchResults;
	}
}
