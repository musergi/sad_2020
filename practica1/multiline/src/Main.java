import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String filepath = args[0];
        FileWriter fileWriter = new FileWriter(filepath);
        MultiLineEditor mle = new MultiLineEditor(new InputStreamReader(System.in));
        fileWriter.write(mle.readLines());
        fileWriter.flush();
        fileWriter.close();
    }
}