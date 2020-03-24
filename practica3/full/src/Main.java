public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")) {
            if (args.length > 1 && args[1].equals("selector")) {
                //TODO: Run server with selector backend
            } else {
                //TODO: Run server with default backend
            }
        } else if (args.length > 1 && args[0].equals("client")) {
            String client = args[1];
            //TODO: Run client
        }
    }
}