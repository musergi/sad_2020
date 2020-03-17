import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class ConsoleView implements PropertyChangeListener {
    private int startingRow;

    public ConsoleView() {
        startingRow = ConsoleUtils.get_cursor_row() - 1;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // Unpack model reference
        Multiline multiline = (Multiline) evt.getNewValue();
        List<StringBuilder> lines = multiline.getLines();

        // Calculate true cursor position
        int consoleWidth = ConsoleUtils.get_console_width();
        int multilineCusorRow = multiline.getCursorRow();
        int trueCursorRow = 0;
        for (int i = 0; i < multilineCusorRow; i++) {
            trueCursorRow += lines.get(i).length() / consoleWidth + 1;
        }
        trueCursorRow += multiline.getCursorColumn() / consoleWidth;
        int trueCursorColumn = multiline.getCursorColumn() % consoleWidth;

        // Move cursor to start
        moveCursorTo(startingRow, 0);

        // Clear screen
        clearFromCursorDown();

        // Print text to screen
        System.out.print(multiline.getFinalString());

        // Set proper cursor position
        moveCursorTo(startingRow + trueCursorRow, trueCursorColumn);
    }

    private void moveCursorTo(int row, int column) {
        System.out.print("\033[" + (row + 1) + ";" + (column + 1) + "H");
    }

    private void clearFromCursorDown() {
        System.out.print("\033[0J");
    }
}