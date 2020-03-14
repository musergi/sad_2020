import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MySocket {
    /**
     * Client
     * funcionalment equivalent a la classe de Java Socket  
     * però que encapsuli excepcions i els corresponents streams de text BufferedReader i PrintWriter. 
     * Aquesta classe haurà de disposar de mètodes de lectura/escriptura dels tipus bàsics.
     */
    Socket socket;
    BufferedReader buff; 
    PrintWriter writer; 

    public MySocket(SocketAddress address, PrintWriter writer){
        //Create connection with the server
        socket = new Socket();
        socket.connect(address);

        this.buff = new BufferedReader(socket.getInputStream());
        this.writer = new PrintWriter(socket.getOutputStream());

    }
    /**
     * Send a message to the client once the connection is made
     * @param message The message that the client has to send
     */
    public void sendMessage(final String message) {
        writer.write(message); //Lo que no se como enviarlo al otro puerto 
        writer.flush();
        writer.close();
    }

    public String receiveMessage() {
        message = buff.readLine();
        buff.close();
        return message;
    }

}