package AOSProject.bin;
import java.util.HashMap;


public class Project {

    public static void main(String[] args) {
        try {
            //when program starts it initializes the node from program arguments
            int identifier = Integer.parseInt(args[0]);
            int portNumber = Integer.parseInt(args[1]);
            MyNode myNode = new MyNode(identifier, portNumber);


            Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("sleep_time")));
            // read config file and create HashMap<Identifier, GeneralNode>
            HashMap<Integer, GeneralNode> nodesHashMap = Helper.readConfigurationFile();
            //start the receiveToken thread
            System.out.println(myNode.identifier + ": Node Starting");
            Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("sleep_time")));
            AcceptConnectionThread acceptConnectionThread = new AcceptConnectionThread(myNode, nodesHashMap);
            acceptConnectionThread.start();

            //wait for some time
            Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("wait_time")));
            SendFirstTokenThread sendFirstTokenThread = new SendFirstTokenThread(myNode, nodesHashMap);
            sendFirstTokenThread.start();

            //wait for accept connection thread to finish
            acceptConnectionThread.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}


