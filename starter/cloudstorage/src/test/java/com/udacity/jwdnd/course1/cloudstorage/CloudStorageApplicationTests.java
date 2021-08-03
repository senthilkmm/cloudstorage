package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static HomePage homePage;
    private static LoginPage loginPage;
    private static ResultPage resultPage;
    private static SignupPage signupPage;

    private static final String baseUrl = "http://localhost:";
    private static final String FIRSTNAME = "tstfn";
    private static final String LASTNAME = "tstln";
    private static final String USERNAME = "tstun";
    private static final String PASSWORD = "tstpwd";

    private static final String NOTE_TITLE = "My note 1";
    private static final String NOTE_TITLE_EDITED = "Edited Note";
    private static final String NOTE_DESCRIPTION = "This is an important item!!";

    private static final String CRED_URL = "www.credurl.com";
    private static final String CRED_USERNAME = "uname";
    private static final String CRED_USERNAME_EDITED = "editedname";
    private static final String CRED_PWD = "unpwd";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        homePage = new HomePage(this.driver);
        loginPage = new LoginPage(this.driver);
        resultPage = new ResultPage(this.driver);
        signupPage = new SignupPage(this.driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get(baseUrl + this.port + "/login");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testHomePageAccessWithoutLoggingIn() {
        navigateToHomePage();
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testHomePageAccessAfterLogout() {
        signup();
        login();
        navigateToHomePage();
        assertEquals("Home", driver.getTitle());
        logout();
        navigateToHomePage();
        assertEquals("Login", driver.getTitle());
    }

    private void signup() {
        driver.get(baseUrl + this.port + "/signup");
        signupPage.signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
    }

    private void login() {
        driver.get(baseUrl + this.port + "/login");
        loginPage.login(USERNAME, PASSWORD);
    }

    private void logout() {
        homePage.logout();
    }

    private void navigateToHomePage() {
        driver.get(baseUrl + this.port + "/home");
    }

    @Test
    public void testCreateNote() {
        createFirstNote();
        assertTrue(homePage.isNoteCreated(NOTE_TITLE));
        homePage.deleteNote();
    }


    @Test
    public void testEditNote() {
        createFirstNote();
        homePage.editNote(NOTE_TITLE_EDITED, NOTE_DESCRIPTION);
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToNoteTab();
        assertTrue(homePage.isNoteCreated(NOTE_TITLE_EDITED));
        homePage.deleteNote();
    }

    @Test
    public void testDeleteNote() {
        createFirstNote();
        homePage.deleteNote();
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToNoteTab();
        assertFalse(homePage.isNoteCreated(NOTE_TITLE));
    }

    private void createFirstNote() {
        signup();
        login();
        homePage.createNote(NOTE_TITLE, NOTE_DESCRIPTION);
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToNoteTab();
    }

    @Test
    public void testCreateCredential() {
        createFirstCredential();
        assertTrue(homePage.isCredentialCreated(CRED_USERNAME));
        homePage.deleteCredential();
    }

    @Test
    public void testEditCredential() {
        createFirstCredential();
        homePage.editCredential(CRED_URL, CRED_USERNAME_EDITED, CRED_PWD);
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToCredentialTab();
        assertTrue(homePage.isCredentialCreated(CRED_USERNAME_EDITED));
        homePage.deleteCredential();
    }

    @Test
    public void testDeleteCredential() {
        createFirstCredential();
        homePage.deleteCredential();
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToCredentialTab();
        assertFalse(homePage.isCredentialCreated(CRED_USERNAME));
    }

    private void createFirstCredential() {
        signup();
        login();
        homePage.createCredential(CRED_URL, CRED_USERNAME, CRED_PWD);
        resultPage.navigateToHomePageAfterSuccess();
        homePage.navigateToCredentialTab();
    }

}
