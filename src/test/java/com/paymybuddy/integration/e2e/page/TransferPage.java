package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

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

    private final WebDriver webDriver;

    public TransferPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void transferMoneyToAnUser(String username, Double amount, String currency) {
        Select userSelect = new Select(connectionSelector);
        userSelect.selectByVisibleText(username);
        amountSelector.sendKeys(amount.toString());
        Select currencySelect = new Select(currencySelector);
        currencySelect.selectByVisibleText(currency);
        payButton.click();
    }

    public AddConnectionPage addConnection() {
        addConnectionButton.click();
        return new AddConnectionPage(webDriver);
    }


}
