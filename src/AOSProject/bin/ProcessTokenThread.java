package AOSProject.bin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

class ProcessTokenThread extends Thread {
    Token token;
    MyNode myNode;
    HashMap<Integer, GeneralNode> nodeHashMap;

    public ProcessTokenThread(Token token, MyNode myNode, HashMap<Integer, GeneralNode> nodeHashMap) {
        super("ProcessTokenThread");
        this.token = token;
        this.myNode = myNode;
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void run() {
        super.run();
        try {
            //System.out.println(myNode.identifier + ": Processing Token");
            if (token.isLastNode()) {
                File file = new File (ConfigParser.getStringValue("outputfilepath") + "Output_" + myNode.identifier + ".txt");
                file.getParentFile().mkdirs();
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(myNode.label + " " + token.sum);
                bufferedWriter.close();


                //last node
                //send termination message
                //send broadcast message to all the nodes of completed
                //for loop through all the nodes

                //send token with path first as the identifier. set isCompleted as true
                for (GeneralNode generalNode : nodeHashMap.values()) {
                    int identifier = generalNode.identifier;
                    SendTokenThread sendTokenThread = new SendTokenThread(new Token(identifier, "(" + identifier + ")"), nodeHashMap, myNode, true);
                    sendTokenThread.start();
                }

                System.out.println(myNode.identifier + ": Terminated");
            } else {
                token.processToken(myNode);
                SendTokenThread sendTokenThread = new SendTokenThread(token, nodeHashMap, myNode, false);
                sendTokenThread.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
