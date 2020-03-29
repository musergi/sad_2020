package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the superclass o all server socket implementations it is the 
 * most general socket implementation of the server socket. In it only the
 * generic code commun to all sockets is written.
 */
public abstract class ChatServerSocket {
    public static final int DEFAULT_PORT = 6969;

    protected static final Logger logger = Logger.getLogger("server");

    protected ServerSocket serverSocket;

    /**
     * Constructor that  build the socket with the specified host and port.
     * @param port
     */
    public ChatServerSocket(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Created server socket. Listening on port " + port + ".");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create socket on port " + port + ".");
            System.exit(1);
        }
    }

    /**
     * The constructor that build the socket with default host and port.
     */
    public ChatServerSocket() {
        this(DEFAULT_PORT);
    }

    public abstract void listen() throws IOException;
}