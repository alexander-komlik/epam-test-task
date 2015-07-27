package page.tut;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class TutBy {

    private static final String URL_MATCH = "www.tut.by";

    private WebDriver driver;

    public TutBy(WebDriver driver) {
        if (!driver.getCurrentUrl().contains(URL_MATCH)) {
            throw new IllegalStateException(
                    "This is not the page you are expected. url -> " + driver.getCurrentUrl() + " urlMatch -> " + URL_MATCH
            );
        }

        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
}
