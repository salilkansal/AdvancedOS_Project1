import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectTest {

    @Test
    public void testIfReadingPropertiesFile (){
        Constants constants = new Constants();
        assert constants.upperBound == 11;
    }


    @Test
    public void TestRandomNumber (){

    }

}