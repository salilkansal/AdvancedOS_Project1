package com.skansal.model;

import com.skansal.model.Node;

public class GeneralNode extends Node {
    public String hostname;
    public boolean isCompleted;

    public GeneralNode(int identifier, int portNumber, String hostname) {
        super(identifier, portNumber);
        this.hostname = hostname;
        isCompleted = false;
    }

    @Override
    public String toString() {
        return "com.skansal.model.GeneralNode{" +
                "identifier='" + identifier + '\'' +
                "hostname='" + hostname + '\'' +
                "port no='" + portNumber + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}