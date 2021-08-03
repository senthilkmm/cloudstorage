package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;


public class ResultPage {
    @FindBys({@FindBy(id = "result-success"),
            @FindBy(tagName = "a")})
    private WebElement homeLinkInSuccess;

    @FindBys({@FindBy(id = "result-error"),
            @FindBy(tagName = "a")})
    private WebElement homeLinkInError;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void navigateToHomePageAfterSuccess() {
        homeLinkInSuccess.click();
    }
}
