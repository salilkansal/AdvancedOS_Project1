import java.util.HashMap;

class ProcessTokenThread extends Thread {
    private Token token;
    private MyNode myNode;
    private HashMap<Integer, GeneralNode> nodeHashMap;

    ProcessTokenThread(Token token, MyNode myNode, HashMap<Integer, GeneralNode> nodeHashMap) {
        super("ProcessTokenThread");
        this.token = token;
        this.myNode = myNode;
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void run() {
        super.run();
        //System.out.println(myNode.identifier + ": Processing Token");
        if (token.isLastNode()) {
            //last node

            //write output File
            Helper.writeOutputFile(myNode, token);

            //send token with path first as the identifier. set isCompleted as true
            //send termination message
            //for loop through all the nodes
            for (GeneralNode generalNode : nodeHashMap.values()) {
                int identifier = generalNode.identifier;
                SendTokenThread sendTokenThread = new SendTokenThread(new Token(identifier, "(" + identifier + ")"), nodeHashMap, myNode, true);
                sendTokenThread.start();
            }

            System.out.println(myNode.identifier + ": Terminated");
        } else {
            //normal behaviour of token i.e. processing it (removing current identifier from the path and adding label to the sum)
            token.processToken(myNode);
            //sending the token to the node which is the next in the path.
            SendTokenThread sendTokenThread = new SendTokenThread(token, nodeHashMap, myNode, false);
            sendTokenThread.start();
        }
    }

}
