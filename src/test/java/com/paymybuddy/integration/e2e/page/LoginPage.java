package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    @FindBy(id = "username")
    private WebElement usernameField;
    @FindBy(id = "password")
    private WebElement passwordField;
    @FindBy(id = "login-button")
    private WebElement connectButton;
    @FindBy(id = "create-account-link")
    private WebElement createAccountLink;
    @FindBy(id = "login-card")
    private WebElement loginCard;

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

    public CreateAccountPage goToCreateAccount() {
        createAccountLink.click();
        return new CreateAccountPage(webDriver);
    }

    public void isLoginPageDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(2));
        waiter.until(ExpectedConditions.visibilityOf(loginCard)).isDisplayed();
    }
}
