import java.util.Scanner;

public class Console {
    public static void setRawMode() {
        CommandRunner.run("stty raw < /dev/tty");
    }

    public static void disableEcho() {
        CommandRunner.run("stty -echo < /dev/tty");
    }

    public static void enableEcho() {
        CommandRunner.run("stty echo < /dev/tty");
    }

    public static void setCookedMode() {
        CommandRunner.run("stty cooked < /dev/tty");
    }

    public static Size getSize() {
        String output = CommandRunner.run("stty -a < /dev/tty");
        int rows, cols;

        Scanner scanner = new Scanner(output);
        rows = Integer.parseInt(scanner.findInLine("[0-9]+(?= rows)"));
        cols = Integer.parseInt(scanner.findInLine("[0-9]+(?= columns)"));

        return new Size(rows, cols);
    }

    static class Size {
        public int rows;
        public int columns;

        public Size(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
        }

        public String toString() {
            return new StringBuilder().append('(').append(rows).append(',').append(columns).append(')').toString();
        }
    }
}