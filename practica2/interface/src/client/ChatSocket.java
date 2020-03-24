package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatSocket {
    public static final String HOST_NAME = "localhost";
    public static final int PORT = 6969;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String nick;

    public ChatSocket(String nick) {
        try {
            socket = new Socket(HOST_NAME, PORT);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.nick = nick;
    }


    public void sendMessage(final String target, final String message) {
        out.println("SENDMESSAGE " + nick + " " + target);
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        return in.readLine();
    }

}