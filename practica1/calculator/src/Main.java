import java.io.File;

public class Main {
    private static void loadLibraries() {
        String mainFolder = new File(Main.class.getResource("Main.class").getFile()).getParent();
        System.load(mainFolder + "/libconsole_utils.so");
        ConsoleUtils.init_console();
    }

    public static void main(String[] args) {
        loadLibraries();
        new Controller().start();
    }
}