package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddConnectionPage {
    @FindBy(id = "add-user-selector")
    private WebElement userSelector;
    @FindBy(id = "add-connection-button")
    private WebElement addConnectionBtn;

    private final WebDriver webDriver;

    public AddConnectionPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void addConnection(String username) {
        Select userSelect = new Select(userSelector);
        userSelect.selectByVisibleText(username);
        addConnectionBtn.click();
    }


}
