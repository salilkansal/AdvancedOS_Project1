package AOSProject.bin;

import org.junit.Test;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.Assert.*;


public class HelperTest {
    @Test
    public void getStartToken1() throws Exception {
        for (int i=0; i<5; i++)
            System.out.println(Helper.getStartToken(i));

    }

    @Test
    public void readConfigurationFileTest() throws Exception {
        System.out.println(Helper.readConfigurationFile());
    }

    @Test
    public void parseConfigTest() throws Exception {
        System.out.println(Helper.parseConfig());
    }

    @Test
    public void getCurrentTimeStamp() throws Exception {
        System.out.println(Helper.GetCurrentTimeStamp());
    }

    @Test
    public void readConfigurationFile() throws Exception {
        HashMap<Integer, GeneralNode> nodeHashMap =  Helper.readConfigurationFile();
        assert nodeHashMap.size()== 5;
    }

    @Test
    public void getStartToken() throws Exception {
        System.out.println(Helper.getStartToken(2));
    }
}