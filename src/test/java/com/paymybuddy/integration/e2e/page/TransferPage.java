package com.paymybuddy.integration.e2e.page;

import com.paymybuddy.model.Currency;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TransferPage {
    @FindBy(id = "add-connection-btn")
    private WebElement addConnectionButton;
    @FindBy(id = "to-user-selector")
    private WebElement connectionSelector;
    @FindBy(id = "transfer-amount")
    private WebElement amountSelector;
    @FindBy(id = "currency-selector")
    private WebElement currencySelector;
    @FindBy(id = "pay-button")
    private WebElement payButton;
    @FindBy(id = "send-money-card")
    private WebElement transferCardDiv;
    @FindBy(id = "exampleModal")
    private WebElement descriptionModal;
    @FindBy(id = "transaction-description")
    private WebElement transactionDescription;
    @FindBy(id = "modal-pay-button")
    private WebElement modalPayButton;
    @FindBy(id = "not-enough-money-alert")
    private WebElement notEnoughMoneyAlert;

    private final WebDriver webDriver;

    public TransferPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public TransferPage transferMoneyToAnUser(String username, Double amount, Currency currency, String description) {
        Select userSelect = new Select(connectionSelector);
        userSelect.selectByVisibleText(username);
        amountSelector.clear();
        amountSelector.sendKeys(amount.toString());
        Select currencySelect = new Select(currencySelector);
        currencySelect.selectByVisibleText(currency.getDisplayValue());
        payButton.click();
        isDescriptionModalDisplayed();
        transactionDescription.sendKeys(description);
        modalPayButton.click();

        return new TransferPage(webDriver);
    }

    public AddConnectionPage addConnection() {
        addConnectionButton.click();
        return new AddConnectionPage(webDriver);
    }

    public void isTransferPageDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        waiter.until(ExpectedConditions.visibilityOf(transferCardDiv)).isDisplayed();
    }

    public void isDescriptionModalDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        waiter.until(ExpectedConditions.visibilityOf(descriptionModal)).isDisplayed();
    }

    public boolean isNotEnoughMoneyAlertDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        return waiter.until(ExpectedConditions.visibilityOf(notEnoughMoneyAlert)).isDisplayed();
    }


}
