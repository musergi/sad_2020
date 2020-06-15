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

        //Set default frame configuration 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridBagLayout());
        
        //Create the container which will have the three different panels
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.setBackground(new Color(220,247,246));
        contentPanel.setLayout(new CardLayout());

        // Create the different panels 
        loginPanel = new LoginPanel(contentPanel, client);
        chatSelectionPanel = new ChatSelectionPanel(contentPanel, client);
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
    private Boolean firstClick;

    public LoginPanel(JPanel panel, Client client) {
        this.client = client;
        contentPanel = panel;
        firstClick = true; 

        setBackground(new Color(220,247,246));
        setLayout(new GridBagLayout());
        
        //Create login text button
        JTextArea loginText = new JTextArea("LOGIN");
        loginText.setBackground(new Color(220,247,246));
        add(loginText, GridGenerator.generate(1, 0, 1, 1, 1.0));
        //Create a username text field
        JTextField usernameTextField = new JTextField("Username", 30);
        usernameTextField.setForeground(Color.GRAY);
        usernameTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
                if (firstClick){
                    usernameTextField.setText("");
                    usernameTextField.setForeground(Color.BLACK);
                    firstClick ^= firstClick;
                }
			}
		});
        add(usernameTextField, GridGenerator.generate(1, 1, 1, 1, 1.0));
        //Create a button
        JButton usernameConfirmButton = new JButton("Enter chats");
        usernameConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Save the username
                client.setUsername(usernameTextField.getText());
                //Change the panel
                CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                cardLayout.next(contentPanel);
            }
        });
        add(usernameConfirmButton, GridGenerator.generate(2, 1, 1, 1, 0.0));

        setVisible(true);
    }
}

class ChatSelectionPanel extends JPanel {
    private JPanel contentPanel;
    private Client controller;
    private Boolean firstClick;

    public ChatSelectionPanel(JPanel panel, Client client) {
        contentPanel = panel;
        controller = client;
        firstClick = true;

        setBackground(new Color(220,247,246));
        setLayout(new GridBagLayout());

        JTextArea loginText = new JTextArea("OPEN CHAT");
        loginText.setBackground(new Color(220,247,246));
        add(loginText, GridGenerator.generate(1, 0, 1, 1, 1.0));

        JTextField remoteTextField = new JTextField("Enter a friend's name", 30);
        remoteTextField.setForeground(Color.GRAY);
        remoteTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
                if (firstClick){
                    remoteTextField.setText("");
                    remoteTextField.setForeground(Color.BLACK);
                    firstClick ^= firstClick;
                }
			}
		});
        add(remoteTextField, GridGenerator.generate(1, 1, 1, 1, 1.0));

        JButton usernameConfirmButton = new JButton("Enter");
        usernameConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                controller.setRemoteusername(remoteTextField.getText());
                controller.openChat();
                //Change the panel
                CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
                cardLayout.next(contentPanel);
            }
        });
        add(usernameConfirmButton, GridGenerator.generate(2, 1, 1, 1, 0.0));

        setVisible(true);
    }
}


class ChatPanel extends JPanel {
    private JPanel contentPanel;
    private Client controller;

    public ChatPanel(JPanel panel, Client client, ListModel<String> messages) {
        contentPanel = panel;
        controller = client;

        setBackground(new Color(220,247,246));
        setLayout(new GridBagLayout());

        JList<String> messageList = new JList<>(messages);
        add(messageList, GridGenerator.generate(1, 0, 1, 1, 1.0));

        JTextField messageTextField = new JTextField(30);
        add(messageTextField, GridGenerator.generate(1, 1, 1, 1, 1.0));

        JButton sendMessageButton = new JButton("Send Message");
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String message = messageTextField.getText();
                messageTextField.setText("");
                controller.sendMessage(message);
            }
        });
        add(sendMessageButton, GridGenerator.generate(2, 1, 1, 1, 0.0));

        setVisible(true);
    }
}

class GridGenerator {
    public static GridBagConstraints generate(int x, int y, int width, int height, double weighty) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.weighty = weighty;
        return c;
    }
}