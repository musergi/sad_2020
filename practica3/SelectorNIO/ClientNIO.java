import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class ClientNIO {
    private static SocketChannel client;
    private static ByteBuffer buffer;
    private static ClientNIO instance;
 
    public static final String HOST_NAME = "localhost";
    public static final int PORT = 6969;

    public static ClientNIO start() {
        if (instance == null)
            instance = new ClientNIO();
        return instance;
    }
 
    public static void stop() throws IOException {
        client.close();
        buffer = null;
    }
 
    private ClientNIO() {
        try {
            client = SocketChannel.open(new InetSocketAddress(HOST_NAME, PORT));
            buffer = ByteBuffer.allocate(256);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public String sendMessage(String msg) {
        buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            response = new String(buffer.array()).trim();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
 
    }
}