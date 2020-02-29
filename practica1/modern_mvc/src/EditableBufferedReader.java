import java.io.*;
import java.util.*;

class EditableBufferedReader extends BufferedReader {
    private Line line;

    public EditableBufferedReader(final Reader in) {
        super(in);
        line = new Line(new Console());
    }

    private static void setRaw() throws IOException {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void unsetRaw() throws IOException {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int read() throws IOException {
        while (true) {
            int keyCode = super.read();
            switch (keyCode) {
                case 1:
                    line.home();
                    break;
                case 5:
                    line.end();
                    break;
                case 21:
                    line.delete();
                    break;
                case 27:
                    switch (super.read()) {
                        case 91:
                            switch (super.read()) {
                                case 67:
                                    line.moveCursor(1);
                                    break;
                                case 68:
                                    line.moveCursor(-1);
                                    break;
                            }
                            break;
                    }
                    break;
                case 127:
                    line.backspace();
                    break;
                default:
                    return keyCode;
            }
        }
    }


    public String readLine() throws IOException {
        setRaw();
        int inputChar = 0;
        while((inputChar = read()) != 13) {
            line.addChar((char) inputChar);
        }
        unsetRaw();
        return line.toString();
    } 
}