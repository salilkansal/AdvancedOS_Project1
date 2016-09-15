import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigParser {

    static String CONFIGURATION_PATH = "configuration/config.properties";

    public static String getStringValue(String key) {
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream(CONFIGURATION_PATH);
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}