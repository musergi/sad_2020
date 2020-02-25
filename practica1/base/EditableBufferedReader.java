import java.io.*;
import java.util.*;

class EditableBufferedReader extends BufferedReader {
    private static final Set<Integer> specialChars = new HashSet<>() {{
        add(21);
        add(27);
        add(127);
    }};

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
            String[] cmd = {"/bin/sh", "-c", "stty echo coocked </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int read() throws IOException {
        int keyCode = 0;
        while (specialChars.contains(keyCode = super.read())) {
            switch (keyCode) {
                case 127:
                    line.backspace();
                    break;
            }
        }
        return keyCode;
    }


    public String readLine() throws IOException {
        setRaw();
        int inputChar = 0;
        while((inputChar = read()) != 13) {
            line.addChar((char) inputChar);
            System.out.println(line);
        }
        unsetRaw();
        return line.toString();
    } 
}