package server;

import java.net.ServerSocket;

/**
 * This class is the superclass o all server socket implementations it is the 
 * most general socket implementation of the server socket. In it only the
 * generic code commun to all sockets is written.
 */
public abstract class ChatServerSocket {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 6969;

    protected ServerSocket serverSocket;

    /**
     * Constructor that  build the socket with the specified host and port.
     * @param host
     * @param port
     */
    public ChatServerSocket(String host, int port) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * The constructor that build the socket with default host and port.
     */
    public ChatServerSocket() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public abstract void listen();
}