package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CreateAccountPage {

    @FindBy(id = "firstName")
    private WebElement firstnameInput;
    @FindBy(id = "lastName")
    private WebElement lastnameInput;
    @FindBy(id = "email")
    private WebElement emailInput;
    @FindBy(id = "userName")
    private WebElement usernameInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(id = "passwordConfirmation")
    private WebElement passwordConfirmationInput;
    @FindBy(id = "create-account-btn")
    private WebElement createAccountBtn;
    @FindBy(id = "create-new-account-card")
    private WebElement createAccountCard;
    @FindBy(id = "alert-different-password")
    private WebElement alertDifferentPassword;
    private final WebDriver webDriver;


    public CreateAccountPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public LoginPage createAccount(String firstname, String lastname, String email, String username, String password, String passwordConfirmation) {
        firstnameInput.sendKeys(firstname);
        lastnameInput.sendKeys(lastname);
        emailInput.sendKeys(email);
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        passwordConfirmationInput.sendKeys(passwordConfirmation);
        createAccountBtn.click();

        return new LoginPage(webDriver);
    }

    public void isCreateAccountDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(2));
        waiter.until(ExpectedConditions.visibilityOf(createAccountCard)).isDisplayed();
    }

    public boolean isAlertDifferentPasswordDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(2));
        return waiter.until(ExpectedConditions.visibilityOf(alertDifferentPassword)).isDisplayed();
    }
}
