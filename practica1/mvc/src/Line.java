import java.util.Observable;

public class Line extends Observable {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;

    public Line(Console console) {
        stringBuilder = new StringBuilder();
        addObserver(console);
    }

    public void addChar(char in) {
        stringBuilder.insert(cursor, in);
        cursor++;
        setChanged();
        notifyObservers();
    }

    public void backspace() {
        stringBuilder.deleteCharAt(cursor - 1);
        cursor--;
        setChanged();
        notifyObservers();
    }

    public void delete() {
        stringBuilder.deleteCharAt(cursor);
        setChanged();
        notifyObservers();
    }

    public void moveCursor(int delta) {
        cursor = Math.min(Math.max(cursor + delta, 0), stringBuilder.length());
        setChanged();
        notifyObservers();
    }

    public void home() {
        cursor = 0;
        setChanged();
        notifyObservers();
    }

    public void end() {
        cursor = stringBuilder.length();
        setChanged();
        notifyObservers();
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