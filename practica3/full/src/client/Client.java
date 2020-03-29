package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import client.ui.ChatFrame;

public class Client extends Thread {
    public static String DEFAULT_HOST = "localhost";
    public static int DEFAULT_PORT = 6969;

    private String username;
    private String host;
    private int port;
    private ChatClientSocket socket;
    private DefaultListModel<String> messages;
    
    public Client(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
        messages = new DefaultListModel<>();
    }
        

    public Client(String username) {
        this(DEFAULT_HOST, DEFAULT_PORT, username);
    }

    public void run() {
        new ChatFrame(this, messages);
    }

    public void openChat(String remoteUsername) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                socket = new ChatClientSocket(username, remoteUsername, messages);
                messages.addElement("You connected");
            }
        }).start();
    }
    
    public void sendMessage(String message) {
        messages.addElement(message);
        socket.sendMessage(message);
    }
}