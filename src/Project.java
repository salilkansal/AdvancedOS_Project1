import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;


public class Project {

    public static void main(String[] args) {
        int identifier = Integer.parseInt(args[0]);
        int portNumber = Integer.parseInt(args[1]);

        Node node = new Node(identifier, portNumber);
        
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            new SendRequest(targetHostname, targetPortNumber).start();

            while (true) {
                System.out.println("Waiting for Client Connection");
                Socket clientSocket = serverSocket.accept();
                new IncomingRequest(clientSocket, identifier).start();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}

class RecieveToken extends Thread {

    private Socket socket;
    private Token token;
    private int identifier;

    public RecieveToken(Socket socket, int identifier) {
        this.socket = socket;
        this.identifier = identifier;
    }

    @Override
    public void run() {
        super.run();
        try {
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            String message = "Hello";
            printWriter.println(message + " from Node: " + identifier);
            printWriter.close();
            clientSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class SendToken extends Thread {
    private Token token;

    public SendToken(Token token) {
        this.token = token;
    }

    @Override
    public void run() {
        super.run();
        try {
            Thread.sleep(3000);
            Socket clientSocket = new Socket(targetHostname, targetPortNo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message = reader.readLine();
            System.out.println("Server says:" + message);
            reader.close();
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


