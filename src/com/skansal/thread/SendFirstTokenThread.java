package com.skansal.thread;

import com.skansal.model.GeneralNode;
import com.skansal.Util.Helper;
import com.skansal.model.Token;
import com.skansal.model.MyNode;

import java.util.HashMap;

/**
 *The thread which will send the first token of the identifier
 */
public class SendFirstTokenThread extends Thread {
    private MyNode myNode;
    private HashMap<Integer, GeneralNode> nodesHashMap;
    private Token token;

    public SendFirstTokenThread(MyNode myNode, HashMap<Integer, GeneralNode> nodesHashMap) {
        super("com.skansal.thread.SendFirstTokenThread");
        this.myNode = myNode;
        this.nodesHashMap = nodesHashMap;
        token = Helper.getStartToken(myNode.identifier);
    }

    @Override
    public void run() {
        System.out.println(myNode.identifier + ": Sending First com.skansal.model.Token");
        ProcessTokenThread processTokenThread = new ProcessTokenThread(token, myNode, nodesHashMap);
        processTokenThread.start();
    }
}
