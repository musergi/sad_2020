import java.io.*;

public class EditableBufferedReader extends BufferedReader {
    private static final int KEY_CODE_RETURN = 13;
    private static final int KEY_CODE_BACKSLASH = 127;

    private static final int ESCAPE_CODE = 27;
    private static final int KEY_CODE_ARROW = 91;
    private static final int KEY_CODE_RIGHT_ARROW = 67;
    private static final int KEY_CODE_LEFT_ARROW = 68;

    private Reader reader;
    private Line line;

    public EditableBufferedReader(Reader reader) {
        super(reader);
        this.reader = reader;
        line = new Line();
    }

    private void setRaw() {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unsetRaw() {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int read() throws IOException {
        return reader.read();
    }

    public String readLine() throws IOException {
        setRaw();
        int keyCode = 0;
        while((keyCode = read()) != KEY_CODE_RETURN) {
            line.addInput(keyCode);

            System.out.print("\r");
            System.out.print(line.toString());
        }
        unsetRaw();
        return line.toString();
    }
}