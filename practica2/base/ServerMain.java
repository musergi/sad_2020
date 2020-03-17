package base;

import java.io.IOException;

public class ServerMain {
    public static void main(String args[]) throws IOException {
        MyServerSocket serverSocket = new MyServerSocket(6969);

        serverSocket.listen();
    }
}