package deepak.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */

public class AppTest extends TestBase
{
	@Autowired
	App app;
	
	@Test
	public void AppTestcase() {
		app.hello("deepak");
	}
}
