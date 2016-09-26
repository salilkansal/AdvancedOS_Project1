package AOSProject.bin;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectTest {

    @Test
    public void testIfReadingPropertiesFile (){
        assert Integer.parseInt(ConfigParser.getStringValue("upperbound")) == 10;
        Node node = new Node(1,5000);
        System.out.println(node);
    }

}