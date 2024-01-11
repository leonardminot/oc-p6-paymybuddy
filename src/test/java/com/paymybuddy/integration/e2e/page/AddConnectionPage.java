package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AddConnectionPage {
    @FindBy(id = "add-user-selector")
    private WebElement userSelector;
    @FindBy(id = "add-connection-button")
    private WebElement addConnectionBtn;
    @FindBy(id = "connected-users-card")
    private WebElement connectedUsers;

    private final WebDriver webDriver;

    public AddConnectionPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AddConnectionPage addConnection(String username) {
        Select userSelect = new Select(userSelector);
        userSelect.selectByVisibleText(username);
        addConnectionBtn.click();

        return new AddConnectionPage(webDriver);
    }

    public void isConnectedUserCardDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(5));
        waiter.until(ExpectedConditions.visibilityOf(connectedUsers)).isDisplayed();
    }

}
