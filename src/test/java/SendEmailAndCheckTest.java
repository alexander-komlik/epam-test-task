import entity.Credentials;
import entity.Email;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import page.tut.TutByMailLoginPage;
import page.yandex.SentTab;
import page.yandex.YandexByMailPageTemplate;
import page.yandex.ComposeTab;
import util.CsvUtils;
import util.PropertyUtils;
import util.XmlUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SendEmailAndCheckTest {

    private static final Logger logger = Logger.getLogger(SendEmailAndCheckTest.class);

    private ThreadLocal<WebDriver> driverThread = null;
    private Collection<WebDriver> driverPool = Collections.synchronizedList(new ArrayList<WebDriver>());

    private Credentials credentials;
    private List<Email> emailsToSend;

    @BeforeTest(enabled = true)
    public void open() {
        driverThread = new ThreadLocal<WebDriver>() {
            @Override
            protected WebDriver initialValue() {
                WebDriver ffDriver = new FirefoxDriver();
                ffDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                driverPool.add(ffDriver);
                return ffDriver;
            }
        };

        //login page url, login, password.
        credentials = PropertyUtils.getCredentials();
        //recipient, subject, body
        emailsToSend = XmlUtils.getEmailsToSend();
    }

    @Test(enabled = true, threadPoolSize = 1, invocationCount = 2)
    public void testSendAndCheck() {
        //go to the login page
        getDriver().navigate().to(credentials.getLoginPageUrl());
        TutByMailLoginPage loginPage = new TutByMailLoginPage(getDriver());

        //login in
        YandexByMailPageTemplate inboxTab = loginPage.loginSuccess(credentials.getLogin(), credentials.getPassword());

        Email email = getEmail();

        if (email != null) {
            //go to the compose tab
            ComposeTab composeTab = inboxTab.composeTab();
            //send email
            inboxTab = composeTab.sendMail(email);
            //go to the sent tab
            SentTab sentTab = inboxTab.sentTab();
            //looking for a message in the sent list
            assertTrue(sentTab.checkEmail(email));
            //login out
            sentTab.logout();

            CsvUtils.logSentEmail(email);
        } else {
            //There are no more emails to send.
            inboxTab.logout();
        }
    }

    @AfterTest(enabled = true)
    public void close() {
        for (WebDriver driver : driverPool) {
            driver.quit();
        }
    }

    private WebDriver getDriver() {
        return driverThread.get();
    }

    private Email getEmail() {
        synchronized (emailsToSend) {
            if (!emailsToSend.isEmpty()) {
                return emailsToSend.remove(0);
            } else {
                return null;
            }
        }
    }

}
