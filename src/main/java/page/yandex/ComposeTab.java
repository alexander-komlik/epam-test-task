package page.yandex;

import entity.Email;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Form to send emails.
 */
public class ComposeTab extends YandexByMailPageTemplate {

    public static final Logger logger = Logger.getLogger(ComposeTab.class);

    private static final String TAB_URL_MATCH = "compose";

    /**
     * Email recipient.
     */
    @FindBy(className = "b-yabble__input")
    private WebElement to;

    /**
     * Email subject.
     */
    @FindBy(id = "compose-subj")
    private WebElement subject;

    /**
     * Email body.
     */
    @FindBy(id = "compose-send")
    private WebElement body;

    /**
     * Submit button.
     */
    @FindBy(id = "compose-submit")
    private WebElement bSubmit;

    public ComposeTab(WebDriver driver) {
        super(driver, TAB_URL_MATCH);
        PageFactory.initElements(driver, this);
        logger.debug("Compose tab.");
    }

    /**
     * Send email and move to the inbox tab.
     */
    public YandexByMailPageTemplate sendMail(Email mail) {
        fillForm(mail);
        bSubmit.click();
        return new YandexByMailPageTemplate(driver);
    }

    /**
     * Fill to, subject and body input fields.
     */
    private void fillForm(Email mail) {
        to.sendKeys(mail.getTo());
        subject.sendKeys(mail.getSubject());
        body.sendKeys(mail.getBody());
    }

}
