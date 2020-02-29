import java.util.Scanner;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        MultiLineEditor mle = new MultiLineEditor(new InputStreamReader(System.in));
        System.out.println(mle.readLines());
    }
}