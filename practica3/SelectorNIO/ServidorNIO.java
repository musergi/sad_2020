import java.nio.channels.*;

/**
 * Patron reactor: El patrón de diseño reactor es un patrón de programación concurrente 
 * para manejar los pedidos de servicio entregados de forma concurrente a un manejador de 
 * servicio desde una o más entradas. El manejador de servicio demultiplexa los pedidos entrantes 
 * y los entrega de forma sincrónica a los manejadores de pedidos asociados.
 * 
 * NIO: Defines buffers, which are containers for data, and provides an overview of the other NIO packages.
 */

 public class ServidorNIO {
    Selector selector ;
    ServerSocketChannel serverSocket ;

    public static final String HOST_NAME = "localhost";
    public static final int PORT = 6969;
    
    public ServidorNIO (){
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(HOST_NAME, PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(256); //Allocate new byte buffer
    }

    public void listen() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
 
                SelectionKey key = iter.next();
 
                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }
 
                if (key.isReadable()) {
                    answerWithEcho(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void answerWithEcho(ByteBuffer buffer, SelectionKey key)
      throws IOException {
  
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        if (new String(buffer.array()).trim().equals("POISON_PILL")) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }
 
        buffer.flip();
        client.write(buffer);
        buffer.clear();
    }
 
    private static void register(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {
  
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
 
    public static Process start() throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = EchoServer.class.getCanonicalName();
 
        ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
 
        return builder.start();
    }
 }