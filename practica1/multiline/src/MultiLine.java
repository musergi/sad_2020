import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class MultiLine {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private List<StringBuilder> lines;
    private int cursorRow, cursorColumn;
    private boolean insert;

    public MultiLine(ConsoleView consoleView) {
        lines = new ArrayList<>();
        lines.add(new StringBuilder());
        propertyChangeSupport.addPropertyChangeListener(consoleView);
        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void addChar(int charCode) {
        if (insert) {
            delete();
        }
        lines.get(cursorRow).insert(cursorColumn, (char) charCode);
        cursorColumn++;

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void moveCursorColumn(int delta) {
        cursorColumn = Math.min(Math.max(cursorColumn + delta, 0), lines.get(cursorRow).length());

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void moveCursorRow(int delta) {
        cursorRow = Math.min(Math.max(cursorRow + delta, 0), lines.size() - 1);
        cursorColumn = Math.min(cursorColumn, lines.get(cursorRow).length());

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void toggleInsert() {
        insert = !insert;
    }

    public void home() {
        cursorColumn = 0;

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void end() {
        cursorColumn = lines.get(cursorRow).length();

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void backspace() {
        if (cursorColumn == 0) {
            return;
        }
        lines.get(cursorRow).deleteCharAt(cursorColumn - 1);
        cursorColumn--;

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public void delete() {
        StringBuilder cursorLine = lines.get(cursorRow);
        if (cursorColumn == cursorLine.length()) {
            return;
        }
        cursorLine.deleteCharAt(cursorColumn);

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    } 

    public void newLine() {
        lines.add(new StringBuilder());
        cursorRow++;
        cursorColumn = 0;

        propertyChangeSupport.firePropertyChange("lines", null, getDisplayString());
    }

    public String getDisplayString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\033[2J\033[1;1H");
        for (StringBuilder line: lines) {
            stringBuilder.append(line.toString());
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("\033[").append(cursorRow + 1).append(";").append(cursorColumn + 1).append("H");
        return stringBuilder.toString();
    }
}