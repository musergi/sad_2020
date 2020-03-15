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

    public MySocket(final String hostName, final int port) throws IOException {
        // Create connection with the server
        socket = new Socket(hostName, port);

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);

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