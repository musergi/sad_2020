public class Line {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;

    public Line() {
        stringBuilder = new StringBuilder();
        cursor = 0;
        insert = false;
    }

    public void moveCursor(int delta) {
        int maxCursor = stringBuilder.length() + 1;
        cursor = (cursor + delta + maxCursor) % maxCursor;
    }

    public void moveCursorStart() {
        cursor = 0;
    }

    public void moveCursorEnd() {
        cursor = stringBuilder.length();
    }

    public void addChar(int code) {
        stringBuilder.insert(cursor, (char) code);
        cursor++;
    }

    public void backspace() {
        stringBuilder.deleteCharAt(cursor - 1);
        cursor--;
    }

    public String getDisplayString() {
        StringBuilder displayString = new StringBuilder();
        displayString.append((char) 27);
        displayString.append("[2K");
        displayString.append('\r');
        displayString.append(stringBuilder.toString());
        displayString.append(' ');
        displayString.append("\033[" + (1 + stringBuilder.length() - cursor) + "D");
        return displayString.toString();
    }

    public String toString() {
        return stringBuilder.toString();
    }
}