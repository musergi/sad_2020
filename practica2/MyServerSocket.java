public class MyServerSocket{
    /**
     * Servidor
     * funcionalment equivalent a la classe de Java ServerSocket 
     * però que encapsuli excepcions i els corresponents streams de text BufferedReader i PrintWriter. 
     * Aquesta classe haurà de disposar de mètodes de lectura/escriptura dels tipus bàsics.
     */
    ServerSocket socket;

    public MyServerSocket(){
        //Create connection
        serverSocket = new ServerSocket();
        serverSocket.connection();
        serverSocket.accept();
    }
    
    public void sendMessage(final String message) {

    }

    public String receiveMessage() {
        return null;

    }
}