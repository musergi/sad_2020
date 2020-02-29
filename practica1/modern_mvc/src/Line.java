import java.beans.*;

public class Line {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Line(PropertyChangeListener listener) {
        stringBuilder = new StringBuilder();
        pcs.addPropertyChangeListener(listener);
    }

    public void addChar(char in) {
        stringBuilder.insert(cursor, in);
        cursor++;
        pcs.firePropertyChange("display", null, getDisplayString());
    }

    public void backspace() {
        stringBuilder.deleteCharAt(cursor - 1);
        cursor--;
        pcs.firePropertyChange("display", null, getDisplayString());
    }

    public void delete() {
        stringBuilder.deleteCharAt(cursor);
        pcs.firePropertyChange("display", null, getDisplayString());
    }

    public void moveCursor(int delta) {
        cursor = Math.min(Math.max(cursor + delta, 0), stringBuilder.length());
        pcs.firePropertyChange("display", null, getDisplayString());
    }

    public void home() {
        cursor = 0;
        pcs.firePropertyChange("display", null, getDisplayString());
    }

    public void end() {
        cursor = stringBuilder.length();
        pcs.firePropertyChange("display", null, getDisplayString());
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