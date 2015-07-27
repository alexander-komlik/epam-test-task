package page.yandex;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.tut.TutBy;

import java.util.List;

/**
 * Template for yandex.by mail pages.
 */
public class YandexByMailPageTemplate {

    private static final Logger logger = Logger.getLogger(YandexByMailPageTemplate.class);

    private static final String URL_MATCH = "mail.yandex.by";

    protected WebDriver driver;
    protected WebDriverWait wait;

    /**
     * Inbox button on the sidebar.
     */
//    protected WebElement bInbox;

    /**
     * Sent button on the sidebar.
     */
//    protected WebElement bSent;

    /**
     * Compose button.
     */
    @FindBy(className = "js-toolbar-item-title-compose")
    protected WebElement bCompose;

    @FindBy(className = "js-header-user-name")
    protected WebElement exitDropdown;

    public YandexByMailPageTemplate(WebDriver driver) {
        this(driver, "");
    }

    public YandexByMailPageTemplate(WebDriver driver, String tabUrlMatch) {
        if (!driver.getCurrentUrl().contains(URL_MATCH)) {
            throw new IllegalStateException(
                    "This is not the page you are expected. url -> " + driver.getCurrentUrl() + " urlMatch -> " + URL_MATCH + ", " + tabUrlMatch
            );
        }

        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains(tabUrlMatch));
        this.driver = driver;
    }

    /**
     * Go to the Inbox tab.
     */
    public InboxTab inboxTab() {
        logger.debug("Go to the inbox tab.");
        waitForJsEvent(2000);//avoid popup
        WebElement bInbox = driver.findElement(By.cssSelector("a[href*='inbox']"));
        wait.until(ExpectedConditions.elementToBeClickable(bInbox));
        bInbox.click();
        return new InboxTab(driver);
    }

    /**
     * Go to the Sent tab.
     */
    public SentTab sentTab() {
        logger.debug("Go to the inbox tab.");
        waitForJsEvent(2000);//avoid popup
        WebElement bSent = driver.findElement(By.cssSelector("a[href*='sent']"));
        wait.until(ExpectedConditions.elementToBeClickable(bSent));
        bSent.click();
        return new SentTab(driver);
    }

    /**
     * Go to email compose form.
     */
    public ComposeTab composeTab() {
        logger.debug("Go to the compose form");
        bCompose.click();
        return new ComposeTab(driver);
    }

    /**
     * Logout to close session.
     */
    public TutBy logout() {
        wait.until(ExpectedConditions.elementToBeClickable(exitDropdown));
        exitDropdown.click();
        WebElement bExit = driver.findElement(By.cssSelector("a[href*='action=logout']"));
        wait.until(ExpectedConditions.visibilityOf(bExit));
        bExit.click();
        return new TutBy(driver);
    }

    protected void waitForJsEvent(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //NOP
        }
    }
}
