package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import client.input.SequenceParser;
import client.view.Console;
import client.view.utils.ConsoleUtils;

/**
 * This class represents the chat client side controller. The main use for the
 * controller is to create all the model data structures, create the 
 * communication, and init the view connecting it with the model.
 */
public class Client extends Thread {
    private ChatSocket socket;
    private SequenceParser sequenceParser;
    private Console console;

    /**
     * Loads all the necessary libraries, creates the client UI, and starts the
     * input parser. It also starts all the comunications
     * @param nick
     */
    public Client(String nick) {
        loadConsoleUtils();
        sequenceParser = new SequenceParser(new BufferedReader(new InputStreamReader(System.in)));
        console = new Console();
        // socket = new ChatSocket(nick);
    }

    /**
     * Loads the wrapped console_utils library
     */
    private void loadConsoleUtils() {
        String workingDirectory = System.getProperty("user.dir");
        Path libPath = Paths.get(workingDirectory, "lib", "libconsole_utils.so");
        System.load(libPath.toString());
        ConsoleUtils.init_console();
    }

    public void run() {
        // Set char buffered mode and disable echoing
        ConsoleUtils.set_raw_mode();

        // Select "Menu" as the starting view
        console.setView("Menu");

        try {
            int key;
            while ((key = sequenceParser.next()) != SequenceParser.K_CTRL_D) {
                // Get view to update properly depending on the view
                String view = console.getView();

                if (view.equals("Menu")) {
                    // Update menu
                    if (key == '1') {
                        // TODO: Set chat selection view
                    } else if (key == '2') {
                        break;
                    }
                }

                // TODO: Better update handling system
            }
            // Clear screen, reset cursor and display cursor
            System.out.print("\033[2J\033[1;1H\033[?25h");
        } catch (IOException e) {
            // TODO: Better exception handle
            e.printStackTrace();
        }
    }
}