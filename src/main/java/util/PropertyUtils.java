package util;

import entity.Credentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

    public static final String PROPERTY_FILE_PATH = "target/classes/credentials.properties";
    public static final String PROPERTY_LOGIN_PAGE_URL = "loginPageUrl";
    public static final String PROPERTY_LOGIN = "login";
    public static final String PROPERTY_PASSWORD = "password";

    public static Credentials getCredentials() {

        Properties prop = new Properties();
        InputStream input = null;
        Credentials credentials = null;

        try {

            input = new FileInputStream(PROPERTY_FILE_PATH);

            prop.load(input);

            credentials = new Credentials(
                    prop.getProperty(PROPERTY_LOGIN_PAGE_URL),
                    prop.getProperty(PROPERTY_LOGIN),
                    prop.getProperty(PROPERTY_PASSWORD)
            );

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return credentials;
    }


}
