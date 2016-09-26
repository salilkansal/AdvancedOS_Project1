package AOSProject.bin;

import java.util.HashMap;

/**
 * Created by salilkansal on 9/25/16.
 */
class SendFirstTokenThread extends Thread {
    MyNode myNode;
    HashMap<Integer, GeneralNode> nodesHashMap;
    Token token;

    public SendFirstTokenThread(MyNode myNode, HashMap<Integer, GeneralNode> nodesHashMap) {
        super("SendFirstTokenThread");
        this.myNode = myNode;
        this.nodesHashMap = nodesHashMap;
        token = Helper.getStartToken(myNode.identifier);
    }

    @Override
    public void run() {
        System.out.println(myNode.identifier + ": Sending First Token");
        ProcessTokenThread processTokenThread = new ProcessTokenThread(token, myNode, nodesHashMap);
        processTokenThread.start();
    }
}
