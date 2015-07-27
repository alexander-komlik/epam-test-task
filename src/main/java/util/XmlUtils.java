package util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import entity.Email;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class XmlUtils {

    private static final String TAG_NAME_TO = "to";
    private static final String TAG_NAME_SUBJECT = "subject";
    private static final String TAG_NAME_BODY = "body";

    public static List<Email> getEmailsToSend() {

        List<Email> emailsToSend = new LinkedList<Email>();

        try {
            File fXmlFile = new File("target/classes/emails.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("email");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Email email = new Email();

                    Element eElement = (Element) nNode;

                    email.setTo(eElement.getElementsByTagName(TAG_NAME_TO).item(0).getTextContent());
                    email.setSubject(eElement.getElementsByTagName(TAG_NAME_SUBJECT).item(0).getTextContent());
                    email.setBody(eElement.getElementsByTagName(TAG_NAME_BODY).item(0).getTextContent());

                    emailsToSend.add(email);
                }
            }

        } catch (Exception e) {
            //NOP
        }

        return emailsToSend;
    }
}
