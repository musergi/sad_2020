import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
    private Line line;
    private SequenceParser parser;

    public EditableBufferedReader(final Reader in) {
        super(in);
        parser = new SequenceParser(new BufferedReader(in));
        line = new Line();
    }
    
    /**
     * Sets the console input mode to raw and disables echoing of input
     */
    private static void setRaw() throws IOException {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear the console modifications made by setRaw
     */
    private static void unsetRaw() throws IOException{
        try {
            String[] cmd = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a key code corresponding to the pressed key
     * @return Integer representation of the pressed key
     */
    public int read() throws IOException {
        return parser.next();
    }

    /**
     * Enables the user to input text in the console while enabling him to edit
     * it with some control keys.
     * @return The final string given by the user
     */
    public String readLine() throws IOException {
        setRaw();
        int inputChar = 0;
        while((inputChar = read()) != 13) {
            switch (inputChar) {
                case SequenceParser.K_LEFT:
                    line.moveCursor(-1);
                    break;
                case SequenceParser.K_RIGHT:
                    line.moveCursor(1);
                    break;
                default:
                    line.addChar((char) inputChar);
            }

        }
        unsetRaw();
        return line.toString();
    }
}