package com.gmail.automation.task;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Login {

	public WebDriver driver;
	public WebElement element;
	public String propertyfilepath = "automation.properties";
	public Properties properties = new Properties();
	public WebDriverWait wait;
	public String browserName;

	//Read Properties File
	public void getProperty() {

		try {
			FileInputStream fileStream = new FileInputStream(propertyfilepath);
			properties.load(fileStream);
		} catch (Exception e) {
			System.out.println("\nExpection Details = " + e.getMessage());
		}	
	}

	//Select Browser
	public void checkBrowser() throws IOException, InterruptedException{

		//Check cross browsers
		String browser = properties.getProperty("browser.compare.element");
		if (browser.equalsIgnoreCase(properties.getProperty("firefox.browser.element"))) {

			//setting driver properties
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			String driverPath = properties.getProperty("firefox.driver.path.element");
			System.setProperty("webdriver.firefox.marionette", driverPath);
			driver = new FirefoxDriver(capabilities);

		}
		else if (browser.equalsIgnoreCase(properties.getProperty("chrome.browser.element"))) {

			//setting driver properties
			String driverPath = properties.getProperty("chrome.driver.path.element");
			System.setProperty("webdriver.chrome.driver", driverPath);
			driver = new ChromeDriver();
		}
	}

	//navigate to the URLs
	public void navigateURL(){

		//maximize window
		driver.manage().window().maximize();
		//wait for the element to load
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//navigate driver to mail.google.com
		driver.navigate().to(properties.getProperty("login.url"));
		System.out.println("\nBrowser successfully launch " + driver.getTitle() +" login page ...");
	}

	//fetch Login Details from Properties File
	public void loginInToGmailPage(){

		//Validate Email with wrong input
		if(driver.findElement(By.cssSelector(properties.getProperty("username.error.element"))).isSelected())
		{
			System.out.println("\n*****************************\n");
			System.out.println("Login Fail ! Please Enter Valid Email/Username");
			String actualErrorMessage = driver.findElement(By.cssSelector(properties.getProperty("username.error.element"))).getText();
			System.out.println("The Error is : " + actualErrorMessage);
		}
		else
		{
			//enter email id
			driver.findElement(By.id(properties.getProperty("login.element"))).sendKeys(properties.getProperty("login.username.element"));
			//click on next button
			driver.findElement(By.cssSelector(properties.getProperty("login.submit.element"))).click();
			//wait for 10 second to find password element
			wait = new WebDriverWait(driver,10);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.name(properties.getProperty("password.element"))));
			//enter password element
			driver.findElement(By.name(properties.getProperty("password.element"))).sendKeys(properties.getProperty("login.password.element"));
			//click on next button
			driver.findElement(By.cssSelector(properties.getProperty("password.submit.element"))).click();
			System.out.println("\n*************************************************\n");
			System.out.println("Login Successfully ...");
		}
	}

	//Compose mail and write message in message body and send message
	public void composeMailWindow() {

		//wait for 5 second to find compose element
		wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(properties.getProperty("compose.element"))));
		driver.findElement(By.className(properties.getProperty("compose.element"))).click();

		driver.findElement(By.xpath(properties.getProperty("to.element"))).sendKeys(properties.getProperty("to.emailid.element"));

		driver.findElement(By.cssSelector(properties.getProperty("cc.click.element"))).click();

		driver.findElement(By.xpath(properties.getProperty("cc.element"))).sendKeys(properties.getProperty("cc.emailid.element.1"));
		driver.findElement(By.xpath(properties.getProperty("cc.element"))).clear();
		driver.findElement(By.xpath(properties.getProperty("cc.element"))).sendKeys(properties.getProperty("cc.emailid.element.2"));

		driver.findElement(By.cssSelector(properties.getProperty("subject.element"))).sendKeys(properties.getProperty("subject.title.element"));

		driver.findElement(By.cssSelector(properties.getProperty("mail.body.element"))).sendKeys(properties.getProperty("mail.body.content.element"));

		driver.findElement(By.cssSelector(properties.getProperty("send.button.element"))).click();

		//to handle pop-up after sending message
		try {  
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.findElement(By.xpath(properties.getProperty("profile.element"))).click();
			driver.findElement(By.id(properties.getProperty("logout.element"))).click();
			Alert alt = driver.switchTo().alert();
			alt.accept();
		} catch(NoAlertPresentException noe) {
			noe.getMessage();
		}
		System.out.println("\n*************************************************\n");
		System.out.println("Logout successfully ...");
		System.out.println("\n*************************************************\n");
		System.out.println("Automation Complete ...");
		driver.close();	
	}
}