import com.sun.nio.sctp.SctpChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * The thread which is called by the server in order to actually receive the token and other values from the other connected node
 * This is started once the initial connection is made by the server thread.
 *
 * The protocol is as follows
 *
 * The nodes connect
 * sends identifier
 * send isCompleted flag
 * if isCompleted == true
 * then dont need to send the token
 *
 * Otherwise
 * Send the token object after processing it
 *
 */
class ReceiveTokenThread extends Thread {
    private SctpChannel sctpChannel;
    private MyNode myNode;
    private HashMap<Integer, GeneralNode> nodeHashMap;

    ReceiveTokenThread(SctpChannel sctpChannel, MyNode myNode, HashMap<Integer, GeneralNode> nodeHashMap) {
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
                Token token = receiveToken(sctpChannel);
                ProcessTokenThread processTokenThread = new ProcessTokenThread(token, myNode, nodeHashMap);
                processTokenThread.start();
            }
            sctpChannel.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static ByteBuffer getByteBuffer(SctpChannel sctpChannel) throws IOException {
        @SuppressWarnings("ConstantConditions")
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
