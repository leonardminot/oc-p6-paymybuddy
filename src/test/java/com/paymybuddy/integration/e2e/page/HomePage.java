package com.paymybuddy.integration.e2e.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    @FindBy(id = "home-div")
    private WebElement homeDivElement;
    @FindBy(id = "transfer-page-link")
    private WebElement transferPageLink;
    @FindBy(id = "profile-page-link")
    private WebElement profilePageLink;

    private final WebDriver webDriver;


    public HomePage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public boolean isHomeDisplayed() {
        WebDriverWait waiter = new WebDriverWait(webDriver, Duration.ofSeconds(2));
        return waiter.until(ExpectedConditions.visibilityOf(homeDivElement)).isDisplayed();
    }

    public TransferPage goToTransferPage() {
        transferPageLink.click();
        return new TransferPage(webDriver);
    }

    public ProfilePage goToProfilePage() {
        profilePageLink.click();
        return new ProfilePage(webDriver);
    }
}
