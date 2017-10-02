package com.skansal.thread;

import com.skansal.model.GeneralNode;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;
import com.skansal.model.MyNode;

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
public class AcceptConnectionThread extends Thread {

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
            System.out.println(myNode.identifier + ": Waiting for com.skansal.model.Node to Connect");
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


