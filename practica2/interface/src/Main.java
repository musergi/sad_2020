import java.util.logging.Logger;

import client.Client;
import server.Server;

public class Main {
    private static Logger logger = Logger.getLogger("");
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("server")) {
            Server server = new Server(6969);
            logger.info("Starting server...");
            server.start();
        } else if (args.length == 2 && args[0].equals("client")) {
            Client client = new Client(args[1]);
            client.start();
        }
    }
}