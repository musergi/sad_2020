import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MultiLineEditor extends BufferedReader {
    private MultiLine multiLine;

    public MultiLineEditor(Reader in) {
        super(in);
        multiLine = new MultiLine(new ConsoleView());
    }

    public int read() throws IOException {
        return super.read();
    }

    public String readLines() throws IOException {
        Console.setRawMode();
        Console.disableEcho();

        int charCode = 0;
        while ((charCode = read()) != 4) {
            System.out.print(charCode);
        }

        Console.enableEcho();
        Console.setCookedMode();
        return null;
    }
}