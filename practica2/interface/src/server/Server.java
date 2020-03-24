package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Server extends Thread {
    private static final Logger logger = Logger.getLogger("server");

    private int port;
    private Map<String, Chat> chats;

    public Server(int port) {
        this.port = port;
        chats = new ConcurrentHashMap<>();
    }

    public void run() {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                logger.info("Client connection: " + clientSocket);
                Thread workThread = new Thread(new Worker(clientSocket));
                workThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create server socket, or accept connection");
        }
    }

    private class Worker implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Worker(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    logger.info("Recived: " + line);
                    String[] command = line.split(" ");
                    if ("GETCHAT".equals(command[0])) {
                        String name = Chat.genChatName(command[1], command[2]);
                        List<String> messages = chats.get(name).getMessages();
                        out.println(messages.size());
                        for (String message: messages) {
                            out.println(message);
                        }
                        logger.info("Sent chat: " + name);
                    } else if ("SENDMESSAGE".equals(command[0])) {
                        String name = Chat.genChatName(command[1], command[2]);
                        String message = line = in.readLine();
                        if (!chats.containsKey(name)) {
                            createChat(command[1], command[2]);
                        }
                        Chat chat = chats.get(name);
                        chat.send(command[1], message);
                        logger.info("Added message to chat: " + chat);
                    }
                }

                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                logger.warning("Failed to read intput");
            }
        }

        public void createChat(String user1, String user2) {
            Chat newChat = new Chat(user1, user2);
            chats.put(newChat.getName(), newChat);
            logger.info("Created chat: " + newChat);
        }
    }
}