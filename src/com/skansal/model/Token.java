package com.skansal.model;

import com.skansal.Util.Helper;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class Token implements Serializable {
    private int startIdentifier;
    public LinkedList<Integer> path;
    public int sum;

    public Token(int startIdentifier, String path) {
        this.startIdentifier = startIdentifier;
        this.path = Helper.parsePath(startIdentifier, path);
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
        return "com.skansal.model.Token{" +
                "startIdentifier=" + startIdentifier +
                ", path=" + path +
                ", sum=" + sum +
                '}';
    }

    /**
     * Serializes the object into an array of bytes
     *
     * @param token The input instance of the class com.skansal.model.Token
     * @return the byte[] array serialized of the input object
     */
    public static byte[] serialize(Token token) {
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
     *
     * @param byteBuffer the bytebuffer which contains the object in serialized manner
     * @return com.skansal.model.Token instance of the object in deserialized manner
     */
    public static Token deSerialize(ByteBuffer byteBuffer) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (Token) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
