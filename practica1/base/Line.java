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
        cursor = Math.max(Math.min(cursor + delta, 0), stringBuilder.length());
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

    public String toString() {
        return stringBuilder.toString();
    }
}