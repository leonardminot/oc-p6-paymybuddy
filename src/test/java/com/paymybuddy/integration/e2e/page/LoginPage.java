package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "username")
    private WebElement usernameField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "login-button")
    private WebElement connectButton;

    private final WebDriver webDriver;


    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public HomePage login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        connectButton.click();

        return new HomePage(webDriver);
    }
}
