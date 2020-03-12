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
        addChar((char) keyCode);
    }

    public void addChar(char c) {
        lines.get(cursorRow).append(c);
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