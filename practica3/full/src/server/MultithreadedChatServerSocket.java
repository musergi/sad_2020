package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MultithreadedChatServerSocket extends ChatServerSocket {
    /**
     * Map that stores all the waiting sockets in the format: "host remote"
     */
    private Map<String, Socket> waitingUsers;

    public MultithreadedChatServerSocket(int port) {
        super(port);
        waitingUsers = new HashMap<>();
    }

    public MultithreadedChatServerSocket() {
        this(DEFAULT_PORT);
    }

    @Override
    public void listen() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            logger.info("Connection: " + clientSocket);
            Worker worker = new Worker(clientSocket);
            Thread workerThread = new Thread(worker);
            workerThread.start();
        }
    }

    private class Worker implements Runnable {
        private Socket socket;
        private BufferedReader in;

        public Worker(Socket clientSocket) throws IOException {
            socket = clientSocket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String clientConfig = in.readLine();
                String[] config = clientConfig.split(" "); // Un string del estilo "musergi norma 1.0 ascii"
                String requestUsername = config[0];
                String remoteUsername = config[1];
                
                String searchConnectionString = remoteUsername + " " + requestUsername;
                boolean waiting = waitingUsers.containsKey(searchConnectionString);
                if (!waiting) {
                    waitingUsers.put(requestUsername + " " + remoteUsername, socket);
                    logger.info("Adding " + socket + " to queue. Waiting for " + remoteUsername + " from " + requestUsername + ".");
                } else {
                    Socket remoteSocket = waitingUsers.get(searchConnectionString);
                    logger.info("Connecting " + socket + " to " + remoteSocket + ".");
                    new ForwarderThread(socket, remoteSocket).start();
                    new ForwarderThread(remoteSocket, socket).start();
                }
            } catch (IOException e) {
                logger.warning("Failed to read client configuration. Socket discarded.");
            }
        }

        private class ForwarderThread extends Thread {
            private Socket inputSocket;
            private Socket outputSocket;
            private BufferedReader in;
            private PrintWriter out;
            private PrintWriter confirm_in;

            public ForwarderThread(Socket inputSocket, Socket outputSocket) throws IOException {
                this.inputSocket = inputSocket;
                this.outputSocket = outputSocket;
                in = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
                confirm_in = new PrintWriter(inputSocket.getOutputStream(), true);
                out = new PrintWriter(outputSocket.getOutputStream(), true);
            }

            @Override
            public void run() {
                try {
                    confirm_in.println("OK");
                    while (true) {
                        out.println(in.readLine());
                    }
                } catch (IOException e) {
                    logger.info("Socket " + inputSocket + " closed.");
                }
            }
        }
    }
}