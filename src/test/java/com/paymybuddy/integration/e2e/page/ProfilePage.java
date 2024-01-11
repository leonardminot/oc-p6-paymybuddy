package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfilePage {
    @FindBy(id = "create-bank-account-btn")
    private WebElement createBankAccountButton;
    @FindBy(id = "current-balance-cards")
    private WebElement balancesCardDiv;

    private final WebDriver webDriver;

    public ProfilePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AddBankAccountPage addBankAccount() {
        createBankAccountButton.click();

        return new AddBankAccountPage(webDriver);
    }

    public boolean isProfilePageDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        return waiter.until(ExpectedConditions.visibilityOf(balancesCardDiv)).isDisplayed();
    }
}
