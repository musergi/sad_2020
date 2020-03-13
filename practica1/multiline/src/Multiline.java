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
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Multiline((");
        stringBuilder.append(cursorRow).append(", ").append(cursorColumn);
        stringBuilder.append("), ");
        stringBuilder.append(lines.get(0));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}