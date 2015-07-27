package page.tut;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import page.yandex.InboxTab;

import java.util.List;

public class TutByMailLoginPage {

    private static final String URL_MATCH = "mail.tut.by";

    private WebDriver driver;

    /**
     * Email field.
     */
    @FindBy(name = "login")
    private WebElement fEmail;

    /**
     * Password field.
     */
    @FindBy(name = "password")
    private WebElement fPassword;

    /**
     * Submit button.
     */
    @FindBy(className = "loginButton")
    private WebElement bSubmit;

    /**
     * Error message, size = 1 if a message is present, 0 otherwise.
     */
    @FindBy(xpath = "/html/body/div/div/div/div/div/div/form/fieldset/strong")
    private List<WebElement> errorMessages;

    public TutByMailLoginPage(WebDriver driver) {
        //
        if (!driver.getCurrentUrl().contains(URL_MATCH)) {
            throw new IllegalStateException(
                    "This is not the page you are expected. url -> " + driver.getCurrentUrl() + " urlMatch -> " + URL_MATCH
            );
        }

        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public InboxTab loginSuccess(String email, String password) {
        doLogin(email, password);
        return new InboxTab(driver);
    }

    public TutByMailLoginPage loginFail(String email, String password) {
        doLogin(email, password);
        return new TutByMailLoginPage(driver);
    }

    public String getErrorMessage() {
        if(errorMessages.size() == 0) {
            return null;
        }
        return errorMessages.get(0).getText();
    }

    private void doLogin(String email, String password) {
        fEmail.sendKeys(email);
        fPassword.sendKeys(password);
        bSubmit.click();
    }
}
