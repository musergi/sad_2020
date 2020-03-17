import java.io.IOException;

public class ServerMain {
    public static void main(String args[]) throws IOException {
        MyServerSocket serverSocket = new MyServerSocket(696969);

        serverSocket.listen();
    }
}