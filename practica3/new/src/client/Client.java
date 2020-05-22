package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import client.ui.CardLayoutFrame;

public class Client extends Thread {
    public static String DEFAULT_HOST = "localhost";
    public static int DEFAULT_PORT = 6969;

    private String username;
    private String host;
    private int port;
    private ChatClientSocket socket;
    private DefaultListModel<String> messages;
    
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        messages = new DefaultListModel<>();
    }
        

    public Client() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    

    public void run() {
        new CardLayoutFrame().displayGUI(this, messages);
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}