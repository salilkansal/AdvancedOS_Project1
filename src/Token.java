import java.io.Serializable;
import java.util.List;


class Token implements Serializable {
    int startIdentifier;
    List<Integer> path;
    int sum;

    public Token(int startIdentifier, List<Integer> path, int sum) {
        this.startIdentifier = startIdentifier;
        this.path = path;
        this.sum = sum;
    }

    public boolean isLastNode() {
        return path.size() == 1;
    }

    public void processToken(int label) {
        sum += label;

    }
}
