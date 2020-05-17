package client.ui;

import javax.swing.*;

import client.Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private Client controller;

    public LoginFrame() {
        super("MyChat");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        setLayout(new GridBagLayout());

        JTextField usernameTextField = new JTextField(30);
        add(usernameTextField, genGridConstraint(0, 0, 1, 1));

        JButton usernameConfirmButton = new JButton("Enter chats");
        usernameConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                //Cambiar de frame

            }
        });
        add(usernameConfirmButton, genGridConstraint(1, 0, 1, 1));

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