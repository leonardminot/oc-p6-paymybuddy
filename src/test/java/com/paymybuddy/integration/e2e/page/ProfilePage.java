package com.paymybuddy.integration.e2e.page;

import com.paymybuddy.model.Currency;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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

    public void isProfilePageDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(10));
        waiter.until(ExpectedConditions.visibilityOf(balancesCardDiv)).isDisplayed();
    }

    public ProfilePage addMoneyToAccount(String iban, String amount, Currency currency) {
        WebElement amountForTransaction = webDriver.findElement(By.id("amount_" + iban));
        WebElement currencyForTransaction = webDriver.findElement(By.id("currency_" + iban));
        WebElement addMoneyTransaction = webDriver.findElement(By.id("add-money_" + iban));

        Select currencySelector = new Select(currencyForTransaction);
        currencySelector.selectByVisibleText(currency.getDisplayValue());

        amountForTransaction.clear();
        amountForTransaction.sendKeys(amount);

        addMoneyTransaction.click();
        return new ProfilePage(webDriver);
    }
}
