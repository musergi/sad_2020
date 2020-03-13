import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeSupport;

public class Multiline {
    private int cursorRow, cursorColumn;
    private List<StringBuilder> lines;
    private boolean insert;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Multiline() {
        lines = new ArrayList<>();
        lines.add(new StringBuilder());
        pcs.addPropertyChangeListener("cursor", new CursorView());
        pcs.addPropertyChangeListener("text", new TextView());
    }

    public void process(int keyCode) {
        switch (keyCode) {
            case SequenceParser.K_LEFT:
                moveCursorH(-1);
                break;
            case SequenceParser.K_RIGHT:
                moveCursorH(1);
                break;
            case SequenceParser.K_UP:
                moveCursorV(-1);
                break;
            case SequenceParser.K_DOWN:
                moveCursorV(1);
                break;
            case SequenceParser.K_HOME:
                moveCursorH(-cursorColumn);
                break;
            case SequenceParser.K_END:
                moveCursorH(lines.get(cursorRow).length() - cursorColumn);
                break;
            case SequenceParser.K_BACKSPACE:
                delete(false);
                break;
            case SequenceParser.K_DELETE:
                delete(true);
                break;
            case SequenceParser.K_INSERT:
                insert = !insert;
                break;
            case SequenceParser.K_RETURN:
            case SequenceParser.K_LINE_FEED:
                lineJump();
                break;
            default:
                addChar((char) keyCode);
        }
    }

    public void addChar(char c) {
        StringBuilder currentLine = lines.get(cursorRow);
        if (insert && cursorColumn < currentLine.length()) {
            currentLine.setCharAt(cursorColumn, c);
        } else {
            currentLine.insert(cursorColumn, c);
            pcs.firePropertyChange("text", null, currentLine.substring(cursorColumn));
        }
        cursorColumn++;
        pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(0, 1));
    }

    public void moveCursorH(int delta) {
        int newCursorColumn = cursorColumn + delta;
        if (newCursorColumn == -1 && cursorRow > 0) {
            cursorRow--;
            cursorColumn = lines.get(cursorRow).length();
        } else if (newCursorColumn == lines.get(cursorRow).length() + 1 && cursorRow < lines.size() - 1) {
            cursorRow++;
            cursorColumn = 0;
        } else if (newCursorColumn >= 0 && newCursorColumn <= lines.get(cursorRow).length()) {
            cursorColumn = newCursorColumn;
        }
    }

    public void moveCursorV(int delta) {
        cursorRow = Math.min(Math.max(0, cursorRow + delta), lines.size() - 1);
        int lineLength = lines.get(cursorRow).length();
        if (cursorColumn > lineLength) {
            cursorColumn = lineLength;
        }
    }

    public void delete(boolean right) {
        StringBuilder currentLine =  lines.get(cursorRow);
        int deletePosition = right ? cursorColumn : cursorColumn - 1;
        if (deletePosition >= 0 && deletePosition < currentLine.length()) {
            currentLine.deleteCharAt(deletePosition);
            moveCursorH(right ? 0 : -1);
        } else if (deletePosition == -1 && cursorRow > 0) {
            StringBuilder previousLine = lines.get(cursorRow - 1);
            cursorColumn = previousLine.length();
            previousLine.append(currentLine);
            lines.remove(cursorRow);
            cursorRow--;
        } else if (deletePosition == currentLine.length() && cursorRow < lines.size() - 1) {
            currentLine.append(lines.get(cursorRow + 1));
            lines.remove(cursorRow + 1);
        }
    }

    public void lineJump() {
        // Get current line
        StringBuilder currentLine = lines.get(cursorRow);
        StringBuilder newLine = new StringBuilder();

        // Take text after cursor if there is
        if (cursorColumn != currentLine.length()) {
            newLine.append(currentLine.substring(cursorColumn));
            currentLine.delete(cursorColumn, currentLine.length());
        }

        // Add new line and update cursor position
        lines.add(cursorRow + 1, newLine);
        cursorRow++;
        cursorColumn = 0;
    }

    public String getFinalString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StringBuilder line: lines) {
            if (line != lines.get(0)) {
                stringBuilder.append('\n');
            }
            stringBuilder.append(line.toString());
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Multiline((");
        stringBuilder.append(cursorRow).append(", ").append(cursorColumn);
        stringBuilder.append("), ");
        stringBuilder.append(insert);
        stringBuilder.append(", (");
        stringBuilder.append(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            stringBuilder.append(", ").append(lines.get(i));
        }
        stringBuilder.append("))");
        return stringBuilder.toString();
    }
}
