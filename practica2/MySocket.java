import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket {
    /**
     * Client funcionalment equivalent a la classe de Java Socket però que encapsuli
     * excepcions i els corresponents streams de text BufferedReader i PrintWriter.
     * Aquesta classe haurà de disposar de mètodes de lectura/escriptura dels tipus
     * bàsics.
     */
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public static final String HOST_NAME = "localhost";
    public static final int PORT = 6969;

    public MySocket(String myNick, String remoteNick) throws IOException {
        // Create connection with the server
        socket = new Socket(HOST_NAME, PORT);

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

        sendMessage(myNick);
        sendMessage(remoteNick);

        receiveMessage();
    }

    /**
     * Send a message to the client once the connection is made
     * 
     * @param message The message that the client has to send
     */
    public void sendMessage(final String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

}