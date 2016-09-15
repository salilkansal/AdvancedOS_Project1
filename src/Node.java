import java.util.Random;

/**
 * Created by salilkansal on 9/14/16.
 */
class Node {
    int identifier;
    int portnumber;
    int label;

    public Node(int identifier, int portnumber) {
        this.identifier = identifier;
        this.portnumber = portnumber;
        this.label = generateLabel();
    }

    public int generateLabel() {
        Random random = new Random();
        return random.nextInt(new Constants().upperBound);
    }

}
