package page.yandex;

import entity.Email;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentTab extends YandexByMailPageTemplate {

    private static final Logger logger = Logger.getLogger(SentTab.class);

    private static final String TAB_URL_MATCH = "#sent";

    private static final Pattern PATTERN_SUBJECT = Pattern.compile("<span class=\"b-messages__subject\" title=\"(.*?)\">(.*?)</span>");
    private static final Pattern PATTERN_BODY = Pattern.compile("<span class=\"b-messages__firstline\" title=\"(.*?)\">(.*?)</span>");

    /**
     * Preview of sent emails.
     */
    @FindBy(className = "b-messages__message__link")
    private List<WebElement> sentEmails;

    /**
     * Email body, the only visible element on the list.
     */
    @FindBy(className = "b-message-body__content")
    private List<WebElement> sentEmailBody;

    @FindBy(className = "block-messages-item")
    private List<WebElement> threads;

    public SentTab(WebDriver driver) {
        super(driver, TAB_URL_MATCH);
        PageFactory.initElements(driver, this);
        logger.debug("Sent tab.");
    }

    public boolean checkEmail(Email email) {

        //Opens threads with the required subject.
        openThreads(email);

        //Completion of the thread opening can not be easily detected.
        waitForJsEvent(1000);

        //Searches for the visible previews with the same or empty () subject and similar body.
        List<WebElement> previewsToCheck = getEmailPreviews(email);

        //Iterates found emails and compares them with the reference.
        for (WebElement preview : previewsToCheck) {
            preview.findElement(By.className("b-messages__message__link")).click();

            //Email body is the only visible element on this list.
            String bodyText = null;
            for (WebElement body : sentEmailBody) {
                if (body.isDisplayed()) {
                    bodyText = body.getText();
                    break;
                }
            }

            if (email.getBody().equals(bodyText)) {
                return true;
            }

            //Go to the previous page.
            driver.navigate().back();
        }

        return false;
    }

    /**
     * Opens threads with the reference subject.
     */
    private void openThreads(Email email) {

        List<WebElement> threadsToOpen = new ArrayList<WebElement>();

        for (WebElement thread : threads) {
            if (thread.isDisplayed()) {
                String subject = findSubject(thread.getAttribute("innerHTML"));
                if (email.getSubject().equals(subject)) {
                    threadsToOpen.add(thread);
                }
            }
        }

        for (WebElement thread : threadsToOpen) {
            WebElement arrow = thread.findElement(By.className("b-messages__tcount"));
            arrow.click();
        }
    }

    /**
     * Returns visible previews similar to the source.
     */
    private List<WebElement> getEmailPreviews(Email email) {
        List<WebElement> previews = driver.findElements(By.className("b-messages__message"));
        List<WebElement> previewsToCheck = new ArrayList<WebElement>();
        for (WebElement preview : previews) {
            if (preview.isDisplayed()) {
                String innerHtml = preview.getAttribute("innerHTML");
                String subject = findSubject(innerHtml);
                String bodyPreview = findBodyPreview(innerHtml);
                if (
                        (subject == null || email.getSubject().equals(subject)) &&
                                isSuitableBodyPreview(email.getBody(), bodyPreview)) {
                    previewsToCheck.add(preview);
                }
            }
        }

        return previewsToCheck;
    }

    private String findSubject(String innerHtml) {
        Matcher subjectMatcher = PATTERN_SUBJECT.matcher(innerHtml);
        return subjectMatcher.find() ? subjectMatcher.group(2) : null;
    }

    private String findBodyPreview(String innerHtml) {
        Matcher bodyMatcher = PATTERN_BODY.matcher(innerHtml);
        return bodyMatcher.find() ? bodyMatcher.group(2) : "";
    }

    /**
     * Compare body and body preview, true if body can contain body preview.
     */
    private boolean isSuitableBodyPreview(String body, String bodyPreview) {
        if (bodyPreview.length() == 0) {
            return body.length()  == 0 || body.length() > 200;
        } else {
            return body.startsWith(bodyPreview);
        }
    }
}
