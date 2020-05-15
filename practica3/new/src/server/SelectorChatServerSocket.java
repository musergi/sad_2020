package server;

import java.io.IOException;

/**
 * Selector implementation of the chat server socket
 */
public class SelectorChatServerSocket extends ChatServerSocket {
    public SelectorChatServerSocket(int port) {
        super(port);
        throw new RuntimeException("Not implemented");
    }

    public SelectorChatServerSocket() {
        this(DEFAULT_PORT);
    }

    @Override
    public void listen() throws IOException {
        throw new RuntimeException("Not implemented");
    }
}