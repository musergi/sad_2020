public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("server")) {
            // Run server
        } else if (args.length > 1 && args[0].equals("client")) {
            ClientNIO client = ClientNIO.start();
            client.sendMessage(args[1]);
            ClientNIO.stop();
        }
    }
}