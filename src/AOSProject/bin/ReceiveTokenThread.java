package AOSProject.bin;

import com.sun.nio.sctp.SctpChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Created by salilkansal on 9/25/16.
 */
class ReceiveTokenThread extends Thread {
    private SctpChannel sctpChannel;
    private Token token;
    private MyNode myNode;
    private HashMap<Integer, GeneralNode> nodeHashMap;

    public ReceiveTokenThread(SctpChannel sctpChannel, MyNode myNode, HashMap<Integer, GeneralNode> nodeHashMap) {
        super("ReceiveAndProcessToken");
        this.sctpChannel = sctpChannel;
        this.myNode = myNode;
        this.nodeHashMap = nodeHashMap;
    }

    @Override
    public void run() {
        try {
            //System.out.println(myNode.identifier + ": Receiving Node");
            //receive identifier

            int otherIdentifier = receiveInteger(sctpChannel);
            //System.out.println(myNode.identifier + ": Receiving from Node " + otherIdentifier);

            //receive completed or not completed
            boolean isCompleted = receiveBoolean(sctpChannel);
            //System.out.println(myNode.identifier + ": Node " + otherIdentifier + " (isCompleted: " + isCompleted + ")");

            Helper.setIsCompleted(nodeHashMap, otherIdentifier, isCompleted);

            //receive token if not completed
            if (!isCompleted) {
                token = receiveToken(sctpChannel);
                ProcessTokenThread processTokenThread = new ProcessTokenThread(token, myNode, nodeHashMap);
                processTokenThread.start();
            }
            else {
                //System.out.println(myNode.identifier + "; Setting is completed for Node " + otherIdentifier);
            }
            sctpChannel.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static ByteBuffer getByteBuffer(SctpChannel sctpChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        sctpChannel.receive(byteBuffer, null, null);
        return byteBuffer;
    }

    private static Token receiveToken(SctpChannel sctpChannel) throws IOException {
        ByteBuffer byteBuffer = getByteBuffer(sctpChannel);
        return Token.deSerialize(byteBuffer);
    }

    private static String receiveString(SctpChannel sctpChannel) throws IOException {
        ByteBuffer byteBuffer = getByteBuffer(sctpChannel);
        return new String(byteBuffer.array(), StandardCharsets.UTF_8);
    }

    private static int receiveInteger(SctpChannel sctpChannel) throws IOException {
        ByteBuffer byteBuffer = getByteBuffer(sctpChannel);
        return byteBuffer.getInt(0);
    }
    private static boolean receiveBoolean(SctpChannel sctpChannel) throws IOException {
        String str = receiveString(sctpChannel);
        return Boolean.parseBoolean(str.trim());
    }
}
