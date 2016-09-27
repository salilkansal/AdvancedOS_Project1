import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.util.HashMap;

/**
 * This thread starts the server and then waits for other node
 * to connect to it.
 * It also starts another thread which continuously
 * checks if all threads are terminated and then it closes the server.
 * After receiving a connection request from a node the server node creates
 * a new thread to receive the token and process it
 *
 *
 *
 * @author: salilkansal
 * Create Time: 9/25/16
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
                //noinspection InfiniteLoopStatement
                while (true) {
                    //wait for client connection
                    SctpChannel otherNodeChannel = sctpServerChannel.accept();
                    ReceiveTokenThread receiveTokenThread = new ReceiveTokenThread(otherNodeChannel, myNode, nodeHashMap);
                    receiveTokenThread.start();
                }
            } catch (AsynchronousCloseException ex) {
                //interruption occurs when the timer thread closes the socket.
                //nothing to do as timer already closes the serverChannel.
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

/**
 * This threads continously checks if all nodes are completed.
 * If all nodes are completed it closes the serverChannel.
 *
 */
class CheckIfAllTerminatedThread extends Thread {
    private SctpServerChannel sctpServerChannel;
    private HashMap<Integer, GeneralNode> nodeHashMap;
    private int pollingTime;
    public CheckIfAllTerminatedThread(SctpServerChannel sctpServerChannel, HashMap<Integer, GeneralNode> nodeHashMap) {
        this.sctpServerChannel = sctpServerChannel;
        this.nodeHashMap = nodeHashMap;
        //noinspection ConstantConditions
        pollingTime = Integer.parseInt(ConfigParser.getStringValue("serversockettimeout"));
    }

    @Override
    public void run() {
        super.run();
        //wait for some time and then check if all nodes are completed.
        //if all are completed then close the serverChannel
        try {
            while (!Helper.allNodesCompleted(nodeHashMap))
                Thread.sleep(pollingTime);
            sctpServerChannel.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
