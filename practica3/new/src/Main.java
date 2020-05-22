import client.Client;
import server.*;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")) {
            ChatServerSocket serverSocket;
            if (args.length > 1 && args[1].equals("selector")) {
                if (args.length > 2) {
                    serverSocket = new SelectorChatServerSocket(Integer.parseInt(args[2]));
                } else {
                    serverSocket = new SelectorChatServerSocket();
                }
            } else {
                if (args.length > 1) {
                    serverSocket = new MultithreadedChatServerSocket(Integer.parseInt(args[1]));
                } else {
                    serverSocket = new MultithreadedChatServerSocket();
                }
            }
            Server server = new Server(serverSocket);
            server.start();
        } else if (args.length > 1 && args[0].equals("client")) {
            String clientname = args[1];
            Client client = new Client();
            client.start();
        }
    }
}