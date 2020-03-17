import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    public static void main(final String args[]) throws IOException {
        final MySocket socket = new MySocket(args[0], args[1]);

        final ReaderThread readerThread = new ReaderThread(socket);
        final WriterThread writerThread = new WriterThread(socket);

        new Thread(readerThread).start();
        new Thread(writerThread).start();
    }

    public static class ReaderThread implements Runnable {
        private final MySocket socket;

        public ReaderThread(final MySocket socket) {
            this.socket = socket;
        }

        public void run() {
            while (true) {
                try {
                    System.out.println(socket.receiveMessage());
                } catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static class WriterThread implements Runnable {
        private final MySocket socket;
        private final BufferedReader in;

        public WriterThread(final MySocket socket) {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        public void run() {
            while (true) {
                try {
                    socket.sendMessage(in.readLine());
                } catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}