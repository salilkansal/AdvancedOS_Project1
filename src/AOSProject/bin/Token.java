package AOSProject.bin;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;


class Token implements Serializable {
    int startIdentifier;
    LinkedList<Integer> path;
    int sum;

    public Token(int startIdentifier, String path) {
        this.startIdentifier = startIdentifier;
        this.path = Helper.parsePath(startIdentifier,path);
        this.sum = 0;
    }

    public boolean isLastNode() {
        return path.size() == 1;
    }

    public void processToken(MyNode node) {
            this.sum += node.label;
            this.path.removeFirst();
    }

    @Override
    public String toString() {
        return "Token{" +
                "startIdentifier=" + startIdentifier +
                ", path=" + path +
                ", sum=" + sum +
                '}';
    }

    /**
     * Serializes the object into an array of bytes
     * @param token The input instance of the class Token
     * @return the byte[] array serialized of the input object
     */
    static byte[] serialize(Token token) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(token);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Deserializes the object from the Byte Buffer
     * @param byteBuffer the bytebuffer which contains the object in serialized manner
     * @return Token instance of the object in deserialized manner
     */
    static Token deSerialize(ByteBuffer byteBuffer) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Token token = (Token) objectInputStream.readObject();
            return token;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
