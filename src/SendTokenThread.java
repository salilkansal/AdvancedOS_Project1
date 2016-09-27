import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * This is the client thread. It receives a Token and then send it to the destination node
 * The protocol is ass follows
 *
 * It first sends its own identifier value so receiving node knows from where it is receiving this token
 * Then it sends the isCompleted flag which states whether this is a termination message
 *
 * if the isCompleted flag is true then it does not need to send the Token
 * else the protocol states that Token is sent.
 */
class SendTokenThread extends Thread {
    private Token token;
    private HashMap<Integer, GeneralNode> nodeHashMap;
    private MyNode mynode;
    private boolean isCompleted;

    SendTokenThread(Token token, HashMap<Integer, GeneralNode> nodeHashMap, MyNode mynode, boolean isCompleted) {
        super("SendTokenThread");
        this.token = token;
        this.nodeHashMap = nodeHashMap;
        this.mynode = mynode;
        this.isCompleted = isCompleted;
    }

    @Override
    public void run() {
        try {

            super.run();
            int targetIdentifier = token.path.getFirst();
            String targetHostname = nodeHashMap.get(targetIdentifier).hostname;
            int targetPortNumber = nodeHashMap.get(targetIdentifier).portNumber;

            SocketAddress socketAddress = new InetSocketAddress(targetHostname, targetPortNumber);
            SctpChannel sctpChannel = SctpChannel.open();

            sctpChannel.connect(socketAddress, 1, 1);

            sendInteger(sctpChannel, mynode.identifier);

            sendBoolean(sctpChannel, isCompleted);

            if (!isCompleted) {
                //send Token if isCompleted is False
                sendToken(sctpChannel, token);
            }
            sctpChannel.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void sendToken(SctpChannel sctpChannel, Token token) throws IOException {
        @SuppressWarnings("ConstantConditions") ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
        //noinspection ConstantConditions
        byteBuffer.put(Token.serialize(token));
        byteBuffer.flip();
        sctpChannel.send(byteBuffer, messageInfo);
    }

    private static void sendString(SctpChannel sctpChannel, String string) throws IOException {
        @SuppressWarnings("ConstantConditions") ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
        byteBuffer.put(string.getBytes());
        byteBuffer.flip();
        sctpChannel.send(byteBuffer, messageInfo);

    }

    private static void sendInteger(SctpChannel sctpChannel, int i) throws IOException {
        @SuppressWarnings("ConstantConditions") ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
        byteBuffer.putInt(i);
        byteBuffer.flip();
        sctpChannel.send(byteBuffer, messageInfo);
    }

    private static void sendBoolean(SctpChannel sctpChannel, Boolean bool) throws IOException {
        String s = bool.toString();
        sendString(sctpChannel, s);
    }
}
