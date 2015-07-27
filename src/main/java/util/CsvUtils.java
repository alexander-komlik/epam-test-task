package util;

import entity.Email;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvUtils {

    private static final String FILE_PATH = "target/classes/sentEmails.csv";
    private static final String DELIMITER = ";";

    public static synchronized void logSentEmail(Email email) {

        FileWriter writer = null;

        try
        {
            File file = new File(FILE_PATH);

            //if file doesn't exists, then create it
            if(!file.exists()){
                writer = new FileWriter(file);
                addHeader(writer);
            } else {
                writer = new FileWriter(file, true);
            }

            writer.append(email.getTo()).append(DELIMITER)
                    .append(email.getSubject()).append(DELIMITER)
                    .append(email.getBody()).append("\n");

            writer.flush();
        }
        catch(IOException e) {}
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {}
            }
        }
    }

    private static void addHeader(FileWriter writer) throws IOException {
        writer.append("Recipient").append(DELIMITER)
                .append("Subject").append(DELIMITER)
                .append("Message").append("\n");
    }

}
