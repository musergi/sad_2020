package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyServerSocket {
    /**
     * Servidor funcionalment equivalent a la classe de Java ServerSocket però que
     * encapsuli excepcions i els corresponents streams de text BufferedReader i
     * PrintWriter. Aquesta classe haurà de disposar de mètodes de
     * lectura/escriptura dels tipus bàsics.
     */
    private ServerSocket serverSocket;
    /**
     * String is connectionNick remoteNick
     */
    private Map <String, Socket> pendingConnections = new ConcurrentHashMap<>();

    /**
     * Acceptar clients i quan rep un client el passa a un altre socket
     * 
     * @param port
     * @throws IOException
     */
    public MyServerSocket(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }
    
    public void listen() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new Worker(clientSocket)).start();
        }
    }

    private class Worker implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        
        public Worker (Socket socket) throws IOException {
            clientSocket = socket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public void run(){
            try {
                String connectionNick = in.readLine();
                String remoteNick = in.readLine();

                System.out.println("Connection: " + connectionNick + " " + remoteNick);

                if (!pendingConnections.containsKey(remoteNick + " " + connectionNick)){
                    //Await in queue
                    pendingConnections.put(connectionNick + " " + remoteNick, clientSocket);

                }else {
                    //Start connection
                    Socket remoteSocket = pendingConnections.remove(remoteNick + " " +connectionNick);
                    System.out.println(clientSocket);
                    System.out.println(remoteSocket);
                    new Forwarder(clientSocket, remoteSocket).start();
                    new Forwarder(remoteSocket, clientSocket).start();
                }
  
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private class Forwarder extends Thread {
            private Socket entry; 
            private Socket exit;

            public Forwarder(Socket entry, Socket exit) {
                this.entry = entry;
                this.exit = exit;
            }

            public void run(){
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(entry.getInputStream()));
                    PrintWriter out = new PrintWriter(exit.getOutputStream(), true);

                    out.println("Oki");
                    System.out.println(in);
                    System.out.println(out);

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        out.println(inputLine);
                        System.out.println(inputLine);
                    }
                    in.close();
                    out.close();
                    entry.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}