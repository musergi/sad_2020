package server;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class runs the server, to do so one of the possible socket backend is
 * passed to it. It then invokes the listen method to start the socket
 * listening.
 */
public class Server extends Thread {
    private static final Logger logger = Logger.getLogger("server");

    private ChatServerSocket socket;

    public Server(ChatServerSocket socket) {
        this.socket = socket;
    }

    public void run() {
        logger.info("Starting listening socket");
        try {
            socket.listen();
        } catch (IOException e) {
            logger.warning("Socket failed to accept incomming socket");
        }
    }
}