package AOSProject.bin;

import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.AsynchronousCloseException;
import java.util.HashMap;

/**
 * Created by salilkansal on 9/25/16.
 */
class AcceptConnectionThread extends Thread {

    private MyNode myNode;
    private HashMap<Integer, GeneralNode> nodeHashMap;

    public AcceptConnectionThread(MyNode myNode, HashMap<Integer, GeneralNode> nodeHashMap) {
        super("AcceptConnection");
        this.myNode = myNode;
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void run() {
        try {
            //Starting the sctp server at port number
            SctpServerChannel sctpServerChannel = SctpServerChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(myNode.portNumber);
            sctpServerChannel.bind(inetSocketAddress);

            //start timer thread
            new CheckIfAllTerminatedThread(sctpServerChannel, nodeHashMap).start();
            System.out.println(myNode.identifier + ": Waiting for Node to Connect");
            try {
                while (true) {
                    //wait for client connection
                    SctpChannel otherNodeChannel = sctpServerChannel.accept();
                    ReceiveTokenThread receiveTokenThread = new ReceiveTokenThread(otherNodeChannel, myNode, nodeHashMap);
                    receiveTokenThread.start();
                }
            } catch (AsynchronousCloseException ex) {
                //interruption occurs when the timer thread closes the socket.
                //System.out.println(myNode.identifier + ": Socket Closed. Bye!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class CheckIfAllTerminatedThread extends Thread {
    SctpServerChannel sctpServerChannel;
    HashMap<Integer, GeneralNode> nodeHashMap;

    public CheckIfAllTerminatedThread(SctpServerChannel sctpServerChannel, HashMap<Integer, GeneralNode> nodeHashMap) {
        this.sctpServerChannel = sctpServerChannel;
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void run() {
        super.run();
        //wait for some time and then close the sctp server channel
        try {
            while (!Helper.allNodesCompleted(nodeHashMap))
                Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("serversockettimeout")));
            sctpServerChannel.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
