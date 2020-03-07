import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
    public EditableBufferedReader(final Reader in) {
        super(in);
    }
    
    /**
     * Sets the console input mode to raw and disables echoing of input
     */
    private static void setRaw() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Clear the console modifications made by setRaw
     */
    private static void unsetRaw() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Returns a key code corresponding to the pressed key
     * @return Integer representation of the pressed key
     */
    public int read() throws IOException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enables the user to input text in the console while enabling him to edit
     * it with some control keys.
     * @return The final string given by the user
     */
    public String readLine() throws IOException {
        throw new RuntimeException("Not implemented");
    }
}