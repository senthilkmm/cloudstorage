package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {
    private WebDriverWait wait;

    @FindBy(id = "logoutDiv")
    private WebElement logoutDiv;

//    notes elements
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    @FindBys({@FindBy(id = "nav-notes"),
              @FindBy(tagName = "button")})
    private WebElement addNewNodeButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmitButton;

    @FindBys({@FindBy(id = "userTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr")})
    private List<WebElement> noteList;

    @FindBys({@FindBy(id = "userTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr"),
            @FindBy(tagName = "button")})
    private WebElement editNoteButton;

    @FindBys({@FindBy(id = "userTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr"),
            @FindBy(linkText = "Delete")})
    private WebElement deleteNoteButton;

//    credentials elements
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBys({@FindBy(id = "nav-credentials"),
            @FindBy(tagName = "button")})
    private WebElement addNewCredentialButton;

    @FindBy(id = "credential-url")
    private WebElement credUrlField;

    @FindBy(id = "credential-username")
    private WebElement credUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credPwdField;

    @FindBy(id = "credentialSubmit")
    private WebElement credSubmitButton;

    @FindBys({@FindBy(id = "credentialTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr")})
    private List<WebElement> credList;

    @FindBys({@FindBy(id = "credentialTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr"),
            @FindBy(tagName = "button")})
    private WebElement editCredentialButton;

    @FindBys({@FindBy(id = "credentialTable"),
            @FindBy(tagName = "tbody"),
            @FindBy(tagName = "tr"),
            @FindBy(linkText = "Delete")})
    private WebElement deleteCredentialButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 10);
    }

    public void logout() {
        logoutDiv.findElement(By.tagName("button")).submit();
    }

    public void createNote(String noteTitle, String noteDescription) {
        navigateToNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(addNewNodeButton));
        addNewNodeButton.click();
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteTitleField.sendKeys(noteTitle);
        noteDescriptionField.sendKeys(noteDescription);
        noteSubmitButton.submit();
    }

    public boolean isNoteCreated(String noteTitle) {
        boolean isVisible = false;
        System.out.println("len***: " + noteList.size());
        for (WebElement note : noteList) {
            wait.until(ExpectedConditions.visibilityOf(note));
            if (note.getText().contains(noteTitle)) {
                isVisible = true;
            }
        }
        return isVisible;
    }

    public void navigateToNoteTab() {
        wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        notesTab.click();
    }

    public void editNote(String noteTitle, String noteDescription) {
        navigateToNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(editNoteButton));
        editNoteButton.click();
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteTitleField.clear();
        noteTitleField.sendKeys(noteTitle);
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(noteDescription);
        noteSubmitButton.submit();
    }

    public void deleteNote() {
        navigateToNoteTab();
        wait.until(ExpectedConditions.elementToBeClickable(deleteNoteButton));
        deleteNoteButton.click();
    }

    public void createCredential(String credUrl, String credUsername, String credPwd) {
        navigateToCredentialTab();
        wait.until(ExpectedConditions.elementToBeClickable(addNewCredentialButton));
        addNewCredentialButton.click();
        wait.until(ExpectedConditions.visibilityOf(credUrlField));
        credUrlField.sendKeys(credUrl);
        credUsernameField.sendKeys(credUsername);
        credPwdField.sendKeys(credPwd);
        credSubmitButton.submit();
    }

    public void navigateToCredentialTab() {
        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
        credentialsTab.click();
    }

    public void deleteCredential() {
        navigateToCredentialTab();
        wait.until(ExpectedConditions.elementToBeClickable(deleteCredentialButton));
        deleteCredentialButton.click();
    }

    public boolean isCredentialCreated(String credUsername) {
        boolean isVisible = false;
        System.out.println("len***: " + credList.size());
        for (WebElement cred : credList) {
            wait.until(ExpectedConditions.visibilityOf(cred));
            if (cred.getText().contains(credUsername)) {
                isVisible = true;
            }
        }
        return isVisible;
    }

    public void editCredential(String credUrl, String credUsernameEdited, String credPwd) {
        navigateToCredentialTab();
        wait.until(ExpectedConditions.elementToBeClickable(editCredentialButton));
        editCredentialButton.click();
        wait.until(ExpectedConditions.visibilityOf(credUrlField));
        credUrlField.clear();
        credUrlField.sendKeys(credUrl);
        credUsernameField.clear();
        credUsernameField.sendKeys(credUsernameEdited);
        credPwdField.clear();
        credPwdField.sendKeys(credPwd);
        credSubmitButton.submit();
    }
}
