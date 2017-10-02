package com.skansal.thread;

import com.skansal.Util.ConfigParser;
import com.skansal.model.GeneralNode;
import com.skansal.Util.Helper;
import com.sun.nio.sctp.SctpServerChannel;

import java.io.IOException;
import java.util.HashMap;

/**
 * This threads continously checks if all nodes are completed.
 * If all nodes are completed it closes the serverChannel.
 *
 */
public class CheckIfAllTerminatedThread extends Thread {
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