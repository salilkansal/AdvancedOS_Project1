package com.skansal;

import com.skansal.Util.ConfigParser;
import com.skansal.Util.Helper;
import com.skansal.model.GeneralNode;
import com.skansal.thread.AcceptConnectionThread;
import com.skansal.thread.SendFirstTokenThread;
import com.skansal.model.MyNode;

import java.util.HashMap;


class Project {

    public static void main(String[] args) {
        try {
            //when program starts it initializes the node from program arguments
            int identifier = Integer.parseInt(args[0]);
            int portNumber = Integer.parseInt(args[1]);
            MyNode myNode = new MyNode(identifier, portNumber);


            //noinspection ConstantConditions
            Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("sleep_time")));
            // read config file and create HashMap<Identifier, com.skansal.model.GeneralNode>
            HashMap<Integer, GeneralNode> nodesHashMap = Helper.getNodesHashMap();
            //start the receiveToken thread
            System.out.println(myNode.identifier + ": com.skansal.model.Node Starting");
            //noinspection ConstantConditions
            Thread.sleep(Integer.parseInt(ConfigParser.getStringValue("sleep_time")));
            AcceptConnectionThread acceptConnectionThread = new AcceptConnectionThread(myNode, nodesHashMap);
            acceptConnectionThread.start();

            //wait for some time
            //noinspection ConstantConditions
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


