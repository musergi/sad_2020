import java.io.*;
import java.util.*;

class EditableBufferedReader extends BufferedReader {
    private Line line;

    public EditableBufferedReader(final Reader in) {
        super(in);
        line = new Line();
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
                    System.out.print(line.getDisplayString());
                    break;
                case 5:
                    line.end();
                    System.out.print(line.getDisplayString());
                    break;
                case 21:
                    line.delete();
                    System.out.print(line.getDisplayString());
                    break;
                case 27:
                    switch (super.read()) {
                        case 91:
                            switch (super.read()) {
                                case 67:
                                    line.moveCursor(1);
                                    System.out.print(line.getDisplayString());
                                    break;
                                case 68:
                                    line.moveCursor(-1);
                                    System.out.print(line.getDisplayString());
                                    break;
                            }
                            break;
                    }
                    break;
                case 127:
                    line.backspace();
                    System.out.print(line.getDisplayString());
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
            System.out.print(line.getDisplayString());
        }
        unsetRaw();
        return line.toString();
    } 
}