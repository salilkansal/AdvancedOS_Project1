package AOSProject.bin;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by salilkansal on 9/25/16.
 */
class SendTokenThread extends Thread {
    private Token token;
    HashMap<Integer, GeneralNode> nodeHashMap;
    MyNode mynode;
    boolean isCompleted;

    public SendTokenThread(Token token, HashMap<Integer, GeneralNode> nodeHashMap, MyNode mynode, boolean isCompleted) {
        super("SendTokenThread");
        this.token = token;
        this.nodeHashMap = nodeHashMap;
        this.mynode = mynode;
        this.isCompleted = isCompleted;
    }

    @Override
    public void run() {
        try {
            //System.out.println(mynode.identifier + ": Sending Token");
            super.run();
            int targetIdentifier = token.path.getFirst();
            String targetHostname = nodeHashMap.get(targetIdentifier).hostname;
            int targetPortnumber = nodeHashMap.get(targetIdentifier).portNumber;

            SocketAddress socketAddress = new InetSocketAddress(targetHostname, targetPortnumber);
            SctpChannel sctpChannel = SctpChannel.open();

            //starting on a port number greater than the previous port number to avoid exception
            //sctpChannel.bind(new InetSocketAddress(mynode.portNumber + new Random().nextInt(50)));

            sctpChannel.connect(socketAddress, 1, 1);

            //send my identifier
            //System.out.println(mynode.identifier + ": Sending Identifier");
            sendInteger(sctpChannel, mynode.identifier);

            //send isCompleted
            //System.out.println(mynode.identifier + ": Sending isCompleted");
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
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
        byteBuffer.put(Token.serialize(token));
        byteBuffer.flip();
        sctpChannel.send(byteBuffer, messageInfo);
    }

    private static void sendString(SctpChannel sctpChannel, String string) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
        MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
        byteBuffer.put(string.getBytes());
        byteBuffer.flip();
        sctpChannel.send(byteBuffer, messageInfo);

    }

    private static void sendInteger(SctpChannel sctpChannel, int i) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(ConfigParser.getStringValue("buffer_size")));
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
