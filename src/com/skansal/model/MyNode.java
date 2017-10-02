package com.skansal.model;


import com.skansal.Util.ConfigParser;

import java.util.Random;

public class MyNode extends Node {
    public int label;

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
        return "com.skansal.model.Node{" +
                "identifier=" + identifier +
                ", portNumber=" + portNumber +
                ", label=" + label +
                '}';
    }
}