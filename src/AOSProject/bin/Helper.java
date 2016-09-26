package AOSProject.bin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.*;

public class Helper {
    public static HashMap<Integer, GeneralNode> readConfigurationFile() {

        HashMap<Integer, GeneralNode> nodeHashMap = new HashMap<>();

        try {
            List<String> input = parseConfig();

            int numberOfNodes = Integer.parseInt(input.get(0));
            for (int i = 1; i <= numberOfNodes; i++) {
                String line1 = input.get(i);
                Scanner scanner = new Scanner(line1);

                Integer identifier = scanner.nextInt();
                String hostname = scanner.next();
                int portno = scanner.nextInt();

                GeneralNode generalNode = new GeneralNode(identifier, portno, hostname);
                nodeHashMap.put(identifier, generalNode);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return nodeHashMap;

    }

    public static List<String> parseConfig() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(ConfigParser.getStringValue("config_path"));
        Scanner scanner = new Scanner(fileInputStream);
        List<String> input = new LinkedList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isEmpty() || line.startsWith("#")) continue;
            input.add(line);
        }
        return input;
    }

    public static String GetCurrentTimeStamp() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    public static Token getStartToken(int identifier) {
        try {
            List<String> input = parseConfig();

            int numberOfNodes = Integer.parseInt(input.get(0));
            for (int i = numberOfNodes + 1; i <= 2 * numberOfNodes; i++) {
                String line1 = input.get(i);
                Scanner scanner = new Scanner(line1);
                int currIdentifier = scanner.nextInt();
                String currPath = scanner.nextLine().trim();
                if(currPath.contains("#")){
                    currPath = currPath.substring(0, currPath.indexOf("#")).trim();
                }

                if (identifier == currIdentifier) {
                    return new Token(identifier, currPath);
                }

            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static LinkedList<Integer> parsePath(Integer identifier, String path){
        LinkedList<Integer> pathList = new LinkedList<>();
        path = path.trim();
        path = path.substring(1,path.length()-1);
        String[] pathArray = path.split(",");
        pathList.add(identifier);
        for(String node:pathArray){
            node = node.trim();
            pathList.add(Integer.parseInt(node));
        }
        pathList.add(identifier);
        return pathList;
    }

    public static boolean allNodesCompleted(HashMap<Integer,GeneralNode> nodeHashMap){
        boolean allNodesCompleted = true;
        for(Map.Entry<Integer, GeneralNode> entry: nodeHashMap.entrySet()){
            allNodesCompleted &= entry.getValue().isCompleted;
        }
        return allNodesCompleted;
    }

    public static void setIsCompleted(HashMap<Integer,GeneralNode> nodeHashMap, int identifier, boolean isCompleted){
            nodeHashMap.get(identifier).isCompleted |= isCompleted;
    }
}

