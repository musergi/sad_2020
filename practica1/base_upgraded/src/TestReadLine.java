import java.io.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class TestReadLine {
    public static void main(String[] args) {
        BufferedReader in = new EditableBufferedReader(
          new InputStreamReader(System.in));
        String str = null;
        try {
          str = in.readLine();
        } catch (IOException e) { e.printStackTrace(); }
        System.out.println("\nLine is: " + str);
      }
}