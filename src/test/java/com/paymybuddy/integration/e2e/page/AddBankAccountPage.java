package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddBankAccountPage {

    @FindBy(id = "add-bank-account-card")
    private WebElement addBankAccountCard;
    @FindBy(id = "iban")
    private WebElement IBANInputField;
    @FindBy(id = "country")
    private WebElement countryInputField;
    @FindBy(id = "create-bank-account-btn")
    private WebElement addBankAccountBtn;

    private final WebDriver webDriver;

    public AddBankAccountPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public boolean isAddBankAccountPageDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(2));
        return waiter.until(ExpectedConditions.visibilityOf(addBankAccountCard)).isDisplayed();
    }

    public ProfilePage createBankAccount(String IBAN, String country) {
        IBANInputField.sendKeys(IBAN);
        countryInputField.sendKeys(country);
        addBankAccountBtn.click();

        return new ProfilePage(webDriver);
    }
}
