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
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enables the user to input text in the console while enabling him to edit
     * it with some control keys.
     * @return The final string given by the user
     */
    public String readLine() throws IOException {
        setRaw();
        unsetRaw();
    }
}