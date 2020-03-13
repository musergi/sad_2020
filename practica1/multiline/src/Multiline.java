import java.util.ArrayList;
import java.util.List;

public class Multiline {
    private int cursorRow, cursorColumn;
    private List<StringBuilder> lines;

    public Multiline() {
        lines = new ArrayList<>();
        lines.add(new StringBuilder());
    }

    public void process(int keyCode) {
        switch (keyCode) {
            case SequenceParser.K_LEFT:
                moveCursorH(-1);
                break;
            case SequenceParser.K_RIGHT:
                moveCursorH(1);
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
        lines.get(cursorRow).insert(cursorColumn, c);
        cursorColumn++;
    }

    public void moveCursorH(int delta) {
        cursorColumn = Math.min(Math.max(0, cursorColumn + delta), lines.get(cursorRow).length());
    }

    public void lineJump() {
        lines.add(cursorRow + 1, new StringBuilder());
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
        stringBuilder.append("), (");
        stringBuilder.append(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            stringBuilder.append(", ").append(lines.get(i));
        }
        stringBuilder.append("))");
        return stringBuilder.toString();
    }
}