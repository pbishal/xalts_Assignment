package TestScenarios;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Utility.Excel_Utility;

@Listeners(ExtentReports.ExtentTestNGListener.class)
public class Signin {
	public String baseUrl = "https://xaltsocnportal.web.app/";
	public static WebDriver driver;

	@BeforeTest
	public void setup() {
		System.out.println("Before Test Case Execution");

		// chrome driver setup
		driver = new ChromeDriver();

		// Maximize the window
		driver.manage().window().maximize();

		// opening the url
		driver.get(baseUrl);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	@Test(dataProvider = "signupDetails")
	public void SignupTest(String user, String pwd, String exp) throws InterruptedException {
		System.out.println("The Current Url is: " + driver.getCurrentUrl());

		// click on get started or SIGN IN button
		driver.findElement(By.xpath("//button[normalize-space()='Get Started']")).click();

		driver.findElement(By.cssSelector(
				".MuiButtonBase-root.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.portal-signin-input.signin-alt-button.css-1ujsas3"))
				.click();

		// Find Email field and enter email id
		WebElement email = driver.findElement(By.cssSelector(
				"body > div:nth-child(2) > div:nth-child(1) > main:nth-child(2) > div:nth-child(2) > div:nth-child(2) > div:nth-child(2) > input:nth-child(1)"));
		email.sendKeys(user);

		// Find Password and confirm password field and enter the password
		WebElement password = driver.findElement(By.cssSelector(
				"body > div:nth-child(2) > div:nth-child(1) > main:nth-child(2) > div:nth-child(2) > div:nth-child(4) > div:nth-child(2) > input:nth-child(1)"));
		password.sendKeys(pwd);

		// Click on the Sign Up button
		driver.findElement(By.xpath("//button[normalize-space()='Sign In']")).click();

		String exp_Url = "https://xaltsocnportal.web.app/getting-started";
		String act_Url = driver.getCurrentUrl();

		if (exp.equals("Valid")) {
			if (exp_Url.equals(act_Url)) {
				driver.findElement(By.cssSelector(
						".MuiButtonBase-root.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.MuiButton-root.MuiButton-text.MuiButton-textPrimary.MuiButton-sizeMedium.MuiButton-textSizeMedium.css-1ujsas3"))
						.click();

				Assert.assertTrue(true);

			} else {
				Assert.assertTrue(false);
			}
		} else if (exp.equals("Invalid")) {
			Assert.assertTrue(false);
		}
	}
	
	public void captureScreenshot(Object fileName)
	{
		TakesScreenshot ts=(TakesScreenshot)driver;
		File src=ts.getScreenshotAs(OutputType.FILE);
		File dest=new File(System.getProperty("user.dir")+"//ScreenShots//"+fileName);
		try {
			FileHandler.copy(src, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@DataProvider(name = "signupDetails")
	public String[][] getData() throws IOException {
		String path = ".\\dataFiles\\signinDetails.xlsx";

		Excel_Utility xlutil = new Excel_Utility(path);

		int totalrows = xlutil.getRowCount("Sheet1");
		int totalcols = xlutil.getCellCount("Sheet1", 1);

		String signupDetails[][] = new String[totalrows][totalcols];

		for (int i = 1; i <= totalrows; i++) {
			for (int j = 0; j < totalcols; j++) {
				signupDetails[i - 1][j] = xlutil.getCellData("Sheet1", i, j);
			}
		}
		return signupDetails;
	}

	@AfterTest
	public void tearDown() throws InterruptedException {
		Thread.sleep(5000); // Wait for 5 sec before quit
		driver.close();
		driver.quit();
	}
}
