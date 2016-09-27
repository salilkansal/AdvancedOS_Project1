import org.junit.Test;

import java.util.HashMap;


public class HelperTest {
    @Test
    public void getStartToken1() throws Exception {
        for (int i=0; i<5; i++)
            System.out.println(Helper.getStartToken(i));

    }

    @Test
    public void readConfigurationFileTest() throws Exception {
        System.out.println(Helper.getNodesHashMap());
    }

    @Test
    public void parseConfigTest() throws Exception {
        System.out.println(Helper.getLinesFromConfiguration());
    }



    @Test
    public void readConfigurationFile() throws Exception {
        HashMap<Integer, GeneralNode> nodeHashMap =  Helper.getNodesHashMap();
        assert nodeHashMap.size()== 5;
    }

    @Test
    public void getStartToken() throws Exception {
        System.out.println(Helper.getStartToken(0));
    }
}