package server;

/**
 * Selector implementation of the chat server socket
 */
public class SelectorChatServerSocket extends ChatServerSocket {
    public SelectorChatServerSocket(String host, int port) {
        super(host, port);
        throw new RuntimeException("Not implemented");
    }

    public SelectorChatServerSocket() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    @Override
    public void listen() {
        throw new RuntimeException("Not implemented");
    }
}