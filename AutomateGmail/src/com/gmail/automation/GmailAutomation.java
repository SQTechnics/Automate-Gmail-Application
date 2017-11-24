package com.gmail.automation;

import java.io.IOException;

import com.gmail.automation.task.Login;

public class GmailAutomation {
	
	public static void main(String[] args) throws IOException, InterruptedException  {
		
		Login login = new Login();
		login.getProperty();
		login.checkBrowser();
		login.navigateURL();
		login.loginInToGmailPage();
		login.composeMailWindow();
	}
}