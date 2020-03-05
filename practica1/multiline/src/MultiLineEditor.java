import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MultiLineEditor extends BufferedReader {
    private static final int CHAR_CODE_SOH = 1;
    private static final int CHAR_CODE_ENQ = 5;
    private static final int CHAR_CODE_CR = 13;
    private static final int CHAR_CODE_DELETE = 21;
    private static final int CHAR_CODE_ESC = 27;
    private static final int CHAR_CODE_BACKSPACE = 127;

    private MultiLine multiLine;

    public MultiLineEditor(Reader in) {
        super(in);
        multiLine = new MultiLine(new ConsoleView());
    }

    public int read() throws IOException {
        while (true) {
            int charCode = super.read();
            if (charCode == CHAR_CODE_CR) {
                multiLine.newLine();
            } else if (charCode == CHAR_CODE_BACKSPACE) {
                multiLine.backspace();
            } else if (charCode == CHAR_CODE_DELETE) {
                multiLine.delete();
            } else if (charCode == CHAR_CODE_SOH) { // OSX Home equivalent
                
            } else if (charCode == CHAR_CODE_ENQ) { // OSX End equivalent
                
            } else if (charCode == CHAR_CODE_ESC) {
                charCode = super.read();
                if (charCode == (int) '[') {
                    charCode = super.read();
                    switch ((char) charCode) {
                        case 'A': // Arrow up
                            multiLine.moveCursorRow(-1);
                            break;
                        case 'B': // Arrow down
                            multiLine.moveCursorRow(1);
                            break;
                        case 'C': // Arrow right
                            multiLine.moveCursorColumn(1);
                            break;
                        case 'D': // Arrow left
                            multiLine.moveCursorColumn(-1);
                            break;
                        default:
                            throw new IOException("Unknown escape sequence");
                    }
                }
            } else {
                return charCode;
            }
        }
    }

    public String readLines() throws IOException {
        Console.setRawMode();
        Console.disableEcho();

        int charCode = 0;
        while ((charCode = read()) != 4) {
            multiLine.addChar(charCode);
        }

        Console.enableEcho();
        Console.setCookedMode();
        return null;
    }
}