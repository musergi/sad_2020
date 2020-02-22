import java.io.*;

public class EditableBufferedReader extends BufferedReader {
    private static final int KEY_CODE_RETURN = 13;
    private static final int KEY_CODE_ESC = 27;
    private static final int KEY_CODE_LEFT_BRACKET = 91;
    private static final int KEY_CODE_BACKSPACE = 127;

    private static final int CMD_CURSOR_LEFT = 1;
    private static final int CMD_CURSOR_RIGHT = 2;
    private static final int CMD_CURSOR_HOME = 3;
    private static final int CMD_CURSOR_END = 4;

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
        int characterCode = reader.read();
        if (characterCode == KEY_CODE_ESC) {
            int firstKey = reader.read();
            if (firstKey == KEY_CODE_LEFT_BRACKET) {
                switch((int) reader.read()) {
                    case 'C':
                        return CMD_CURSOR_RIGHT;
                    case 'D':
                        return CMD_CURSOR_LEFT;
                    case 'H':
                        return CMD_CURSOR_HOME;
                }
            }
        }
        return characterCode;
    }

    public String readLine() throws IOException {
        setRaw();
        int keyCode = 0;
        while((keyCode = read()) != KEY_CODE_RETURN) {
            switch (keyCode) {
                case CMD_CURSOR_LEFT:
                    line.moveCursor(-1);
                    break;
                case CMD_CURSOR_RIGHT:
                    line.moveCursor(1);
                    break;
                case CMD_CURSOR_HOME:
                    line.moveCursorStart();
                    break;
                case CMD_CURSOR_END:
                    line.moveCursorEnd();
                    break;
                case KEY_CODE_BACKSPACE:
                    line.backspace();
                    break;
                default:
                    line.addChar(keyCode);
            }
            System.out.print(line.getDisplayString());
        }
        unsetRaw();
        return line.toString();
    }
}