package orasi;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class TestSampleApp {
    WebDriver driver = null;
    private String gridURL = "http://" + System.getProperty("gridHost") + ":" + System.getProperty("gridPort") + "/wd/hub"; //"http://10.238.242.55:4444/wd/hub";
    private String testURL = "http://ec2-54-81-222-88.compute-1.amazonaws.com:8085/SimpleWeb/";
    private WebDriverWait wait = null;
    
    @Test
    public void firefox() throws Exception{
	DesiredCapabilities caps = DesiredCapabilities.firefox();
	driver = new RemoteWebDriver(new URL(gridURL), caps);
	
	Reporter.log("Step 1: Launch SampleApp<br/>");
	driver.get(testURL);	

	wait = new WebDriverWait(driver, 10);
	Assert.assertTrue(wait.until(ExpectedConditions.urlContains("SimpleWeb")));
	Reporter.log("Step 1: Success - SampleApp is launched<br/><br/>");

	Reporter.log("Step 2: Get Jenkins Build Number<br/>");
	String buildNumber = driver.findElement(By.xpath("//*[contains(text(),'This is from Jenkins')]")).getText();
	buildNumber = buildNumber.replace("This is from Jenkins build number ", "");
	int number = Integer.parseInt(buildNumber);
	Reporter.log("Step 2: Success - Build number [" + buildNumber + "] was found.<br/><br/>");
	
	Reporter.log("Step 3: Validate 'Get Current Time' button works<br/>");	
	String dateBefore = driver.findElement(By.xpath("//*[contains(text(),'UTC')]")).getText();
	Thread.sleep(2000);
	driver.findElement(By.tagName("input")).click();
	String dateAfter = driver.findElement(By.xpath("//*[contains(text(),'UTC')]")).getText();
	if(dateBefore.compareTo(dateAfter) == 0) Assert.fail("Dates did not update");
	Reporter.log("Step 3: Success - Date updated from [" + dateBefore + "] to [" + dateAfter + "]<br/><br/>");
    }
    
    @AfterMethod
    public void cleanup(){
	driver.quit();
    }
}
