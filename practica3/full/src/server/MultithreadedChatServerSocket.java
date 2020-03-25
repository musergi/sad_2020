package server;

public class MultithreadedChatServerSocket extends ChatServerSocket {
    public MultithreadedChatServerSocket(String host, int port) {
        super(host, port);
        throw new RuntimeException("Not implemented");
    }

    public MultithreadedChatServerSocket() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }


    @Override
    public void listen() {
        throw new RuntimeException("Not implemented");
    }
}