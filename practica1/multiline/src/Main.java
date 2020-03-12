import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static void loadLibrary() {
        String mainFolder = new File(Main.class.getResource("Main.class").getFile()).getParent();
        System.load(mainFolder + "/libconsole_utils.so");
        ConsoleUtils.init_console();
    }
    public static void main(String[] args) throws IOException {
        loadLibrary();
        System.out.println();
        System.out.println(new MultilineReader(new InputStreamReader(System.in)).readLines());
    }
}
