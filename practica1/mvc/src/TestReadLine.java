import java.io.*;

public class TestReadLine {
    public static void main(String[] args) {
        BufferedReader in = new EditableBufferedReader(new InputStreamReader(System.in));
        String string = null;
        try {
            string = in.readLine(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nInput is: " + string);
    }
}