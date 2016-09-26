package AOSProject.bin;

import java.util.Random;

class Node {
    int identifier;
    int portNumber;

    public Node(int identifier, int portNumber) {
        this.identifier = identifier;
        this.portNumber = portNumber;
    }
}


class MyNode extends Node{
    int label;

    public MyNode(int identifier, int portnumber) {
        super(identifier, portnumber);
        this.label = generateLabel();
    }

    public int generateLabel() {
        Random random = new Random();
        return random.nextInt(Integer.parseInt(ConfigParser.getStringValue("upperbound")) +1);

    }

    @Override
    public String toString() {
        return "Node{" +
                "identifier=" + identifier +
                ", portNumber=" + portNumber +
                ", label=" + label +
                '}';
    }
}

class GeneralNode extends Node{
    String hostname;
    boolean isCompleted;

    public GeneralNode(int identifier, int portNumber, String hostname) {
        super(identifier, portNumber);
        this.hostname = hostname;
        isCompleted = false;
    }

    public void setCompleted(){
        isCompleted = true;
    }

    @Override
    public String toString() {
        return "GeneralNode{" +
                "identifier='" + identifier + '\'' +
                "hostname='" + hostname + '\'' +
                "port no='" + portNumber + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}