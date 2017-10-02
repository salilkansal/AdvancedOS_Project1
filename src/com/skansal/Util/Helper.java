package com.skansal.Util;

import com.skansal.model.GeneralNode;
import com.skansal.model.MyNode;
import com.skansal.model.Token;

import java.io.*;
import java.util.*;

public class Helper {

    /**
     * It reads the configuration file and then builds hashMap of all the nodes
     * This hashMap contains the key as the identifier and the General com.skansal.model.Node object as the value.
     * This object contains all the information about the other nodes like hostname, port no, identifier, isCompleted flag
     */
    public static HashMap<Integer, GeneralNode> getNodesHashMap() {

        HashMap<Integer, GeneralNode> nodeHashMap = new HashMap<>();
        List<String> input = getLinesFromConfiguration();

        assert input != null;
        int numberOfNodes = Integer.parseInt(input.get(0).trim());
        for (int i = 1; i <= numberOfNodes; i++) {
            String line1 = input.get(i);
            Scanner scanner = new Scanner(line1);

            Integer identifier = scanner.nextInt();
            String hostname = scanner.next();
            int portno = scanner.nextInt();

            GeneralNode generalNode = new GeneralNode(identifier, portno, hostname);
            nodeHashMap.put(identifier, generalNode);
        }
        return nodeHashMap;

    }

    /**
     * Given a identifier it tells what is the com.skansal.model.Token object of that identifier
     * This is used by the object to actually send the first token and this creates the token object from the configuration file
     *
     * @param identifier The identifier whose com.skansal.model.Token object is needed- 0
     * @return The token object- com.skansal.model.Token{startIdentifier=0, path=[0, 1, 2, 3, 4, 0], sum=0}
     */
    public static Token getStartToken(int identifier) {
        List<String> input = getLinesFromConfiguration();
        assert input != null;
        int numberOfNodes = Integer.parseInt(input.get(0).trim());
        for (int i = numberOfNodes + 1; i <= 2 * numberOfNodes; i++) {
            String line1 = input.get(i);
            Scanner scanner = new Scanner(line1);
            int currIdentifier = scanner.nextInt();
            String currPath = scanner.nextLine().trim();
            if (currPath.contains("#")) {
                currPath = currPath.substring(0, currPath.indexOf("#")).trim();
            }

            if (identifier == currIdentifier) {
                return new Token(identifier, currPath);
            }
        }
        return null;
    }

    /**
     * This method reads the configuration file and returns a list of
     * all the valid lines, i.e. lines which are not blank and do not start with # (comments)
     *
     * @return List of lines which are not blank and not comments.
     */
    public static List<String> getLinesFromConfiguration() {
        FileInputStream fileInputStream;
        try {
            //noinspection ConstantConditions
            fileInputStream = new FileInputStream(ConfigParser.getStringValue("config_path"));
        } catch (FileNotFoundException e) {
            System.out.println("Configuration File no readable or not present.");
            return null;
        }
        Scanner scanner = new Scanner(fileInputStream);
        List<String> input = new LinkedList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
            input.add(line);
        }
        return input;
    }

    /**
     * This method takes a path in the form of a string and then return a linked list of the actual path
     *
     * @param identifier the start identifier of the path 0
     * @param path       the path in the format (1, 2, 3, 4)
     * @return The linked list path 0, 1, 2, 3, 4, 0
     */
    public static LinkedList<Integer> parsePath(Integer identifier, String path) {
        LinkedList<Integer> pathList = new LinkedList<>();
        path = path.trim();
        path = path.substring(1, path.length() - 1);
        String[] pathArray = path.split(",");
        pathList.add(identifier);
        for (String node : pathArray) {
            node = node.trim();
            pathList.add(Integer.parseInt(node));
        }
        pathList.add(identifier);
        return pathList;
    }

    /**
     * It loops through the HashMap and returns true if all nodes have send termination message
     *
     * @param nodeHashMap The hashmap of nodes which has all the data of the nodes.
     * @return true if all nodes are completed, false otherwise
     */
    public static boolean allNodesCompleted(HashMap<Integer, GeneralNode> nodeHashMap) {
        boolean allNodesCompleted;
        for (Map.Entry<Integer, GeneralNode> entry : nodeHashMap.entrySet()) {
            allNodesCompleted = entry.getValue().isCompleted;
            if (!allNodesCompleted) return false;
        }
        return true;
    }

    /**
     * Given a identifier and isCompleted it or the isCompleted flag for that identifier in the hashMap with the new one.
     * Or is done because even if once the node has been set as iscompleted as true, and if a newer thread then again makes it false.
     *
     * @param nodeHashMap The hashMap of the data for all the nodes.
     * @param identifier  The identifier of the node for which the flag is to be set
     * @param isCompleted The Flag value in boolean
     */
    public static void setIsCompleted(HashMap<Integer, GeneralNode> nodeHashMap, int identifier, boolean isCompleted) {
        nodeHashMap.get(identifier).isCompleted |= isCompleted;
    }


    /**
     * This method writes the output file given a identifier and token
     * @param myNode The Mynode object of the calling com.skansal.model.Node
     * @param token The token that has been completed traversing through its path.
     */
    public static void writeOutputFile(MyNode myNode, Token token) {
        File file = new File (ConfigParser.getStringValue("outputfilepath") + "output_" + String.format("%02d",myNode.identifier) + ".txt");
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(myNode.label + " " + token.sum + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Output File not able to write. ");
        }
    }
}

