package page.yandex;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class InboxTab extends YandexByMailPageTemplate {

    public static final Logger logger = Logger.getLogger(InboxTab.class);

    public static final String TAB_URL_MATCH = "#inbox";

    public InboxTab(WebDriver driver) {
        super(driver, TAB_URL_MATCH);
        PageFactory.initElements(driver, this);
        logger.debug("Inbox tab.");
    }
}
