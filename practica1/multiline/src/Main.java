import java.io.File;
import java.io.IOException;

public class Main {
    private static void loadLibrary() {
        String mainFolder = new File(Main.class.getResource("Main.class").getFile()).getParent();
        System.load(mainFolder + "/libconsole_utils.so");
    }
    public static void main(String[] args) throws IOException {
        loadLibrary();
        ConsoleUtils.set_raw_mode();
        System.out.println(System.in.read());
    }
}