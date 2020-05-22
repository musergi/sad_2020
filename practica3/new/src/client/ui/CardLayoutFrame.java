package client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import client.Client;

public class CardLayoutFrame {
    private JPanel contentPanel;
    private LoginPanel loginPanel;
    private ChatSelectionPanel chatSelectionPanel;
    private ChatPanel chatPanel;

    public void displayGUI(Client client, ListModel<String> messages) {
        JFrame frame = new JFrame("Chats");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new CardLayout());

        // Create the different panels 
        loginPanel = new LoginPanel(contentPanel, client);
        chatSelectionPanel = new ChatSelectionPanel(contentPanel);
        chatPanel = new ChatPanel(contentPanel, client, messages);
        
        // Add the different pannels to the main Frame
        contentPanel.add(loginPanel, "Login Panel"); 
        contentPanel.add(chatSelectionPanel, "Chat Selection Panel");
        contentPanel.add(chatPanel, "Chat Panel");

        frame.setContentPane(contentPanel);
        frame.pack();   
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}

class LoginPanel extends JPanel {
    private JPanel contentPanel;
    private Client client;

    public LoginPanel(JPanel panel, Client client) {
        this.client = client;
        contentPanel = panel;
        setSize(800, 600);
        setLayout(new GridBagLayout());
        
        JTextArea loginText = new JTextArea("LOGIN");
        add(loginText, genGridConstraint(0, 1, 2, 2, 1.0));

        JTextField usernameTextField = new JTextField("Username", 30);
        add(usernameTextField, genGridConstraint(1, 1, 2, 2, 1.0));

        JButton usernameConfirmButton = new JButton("Enter chats");
        usernameConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Change the panel
                CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                cardLayout.next(contentPanel);
            }
        });
        add(usernameConfirmButton, genGridConstraint(1, 2, 1, 1, 0.0));

        setVisible(true);
    }

    private GridBagConstraints genGridConstraint(int x, int y, int width, int height, double weighty) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.weighty = weighty;
        return c;
    }
}


class ChatSelectionPanel extends JPanel {
    private JPanel contentPanel;

    public ChatSelectionPanel(JPanel panel) {
        contentPanel = panel;

        JTextArea loginText = new JTextArea("OPEN CHAT");
        add(loginText, genGridConstraint(0, 1, 2, 2, 1.0));

        JTextField usernameTextField = new JTextField("Enter a friend's name", 30);
        add(usernameTextField, genGridConstraint(1, 1, 2, 2, 1.0));

        JButton usernameConfirmButton = new JButton("Enter");
        usernameConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Change the panel
                CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                cardLayout.next(contentPanel);
            }
        });
        add(usernameConfirmButton, genGridConstraint(1, 2, 1, 1, 0.0));

        setVisible(true);
    }
    private GridBagConstraints genGridConstraint(int x, int y, int width, int height, double weighty) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.weighty = weighty;
        return c;
    }
}


class ChatPanel extends JPanel {
    private JPanel contentPanel;
    private Client controller;

    public ChatPanel(JPanel panel, Client client, ListModel<String> messages) {
        contentPanel = panel;

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