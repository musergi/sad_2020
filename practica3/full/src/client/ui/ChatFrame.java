package client.ui;

import javax.swing.*;

import client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatFrame extends JFrame {
    private Client controller;

    public ChatFrame(Client client, ListModel<String> messages) {
        super("MyChat");
        controller = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setLayout(new GridBagLayout());

        JList<String> messageList = new JList<>(messages);
        add(messageList, genGridConstraint(0, 1, 2, 1));

        JTextField remoteTextField = new JTextField(30);
        add(remoteTextField, genGridConstraint(0, 0, 1, 1));

        JButton remoteConfirmButton = new JButton("Start chat");
        remoteConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String remoteUsername = remoteTextField.getText();
                controller.openChat(remoteUsername);
            }
        });
        add(remoteConfirmButton, genGridConstraint(1, 0, 1, 1));

        JTextField messageTextField = new JTextField(30);
        add(messageTextField, genGridConstraint(0, 2, 1, 1));

        JButton sendMessageButton = new JButton("Send message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String message = messageTextField.getText();
                messageTextField.setText("");
                controller.sendMessage(message);
            }
        });
        add(sendMessageButton, genGridConstraint(1, 2, 1, 1));

        setVisible(true);
    }

    private GridBagConstraints genGridConstraint(int x, int y, int width, int height) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        return c;
    }
}