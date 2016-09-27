import java.util.Random;

class Node {
    int identifier;
    int portNumber;

    Node(int identifier, int portNumber) {
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

    private int generateLabel() {
        Random random = new Random();
        //noinspection ConstantConditions
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