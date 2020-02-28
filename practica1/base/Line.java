public class Line {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;

    public Line() {
        stringBuilder = new StringBuilder();
    }

    public void addChar(char in) {
        stringBuilder.insert(cursor, in);
        cursor++;
    }

    public void backspace() {
        stringBuilder.deleteCharAt(cursor - 1);
        cursor--;
    }

    public void delete() {
        stringBuilder.deleteCharAt(cursor);
    }

    public void moveCursor(int delta) {
        cursor = Math.min(Math.max(cursor + delta, 0), stringBuilder.length());
    }

    public void home() {
        cursor = 0;
    }

    public void end() {
        cursor = stringBuilder.length();
    }

    public void toggleInsert() {
        insert = !insert;
    }

    public String getDisplayString() {
        StringBuilder displayBuilder = new StringBuilder();
        displayBuilder.append('\r');
        displayBuilder.append(stringBuilder.toString());
        displayBuilder.append(" ");
        displayBuilder.append("\033[");
        displayBuilder.append(stringBuilder.length() - cursor + 1);
        displayBuilder.append("D");
        return displayBuilder.toString();
    }

    public String toString() {
        return stringBuilder.toString();
    }
}