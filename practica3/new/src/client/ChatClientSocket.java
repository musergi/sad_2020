package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;

public class ChatClientSocket {
    /**
     * Client funcionalment equivalent a la classe de Java Socket però que encapsuli
     * excepcions i els corresponents streams de text BufferedReader i PrintWriter.
     * Aquesta classe haurà de disposar de mètodes de lectura/escriptura dels tipus
     * bàsics.
     */
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static final String HOST_NAME = "localhost";
    public static final int PORT = 6969;

    public ChatClientSocket(String myNick, String remoteNick, DefaultListModel<String> messages){
        // Create connection with the server
        try {
            socket = new Socket(HOST_NAME, PORT);

            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);

            sendMessage(myNick + " " + remoteNick);
            receiveMessage();

            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            messages.addElement(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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