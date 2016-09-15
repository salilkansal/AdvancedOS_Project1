import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class Constants{
    int upperBound;

    public Constants() {
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            this.upperBound = Integer.parseInt(properties.getProperty("upperbound"));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}