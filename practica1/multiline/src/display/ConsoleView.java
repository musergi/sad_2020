package display;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class in charge to update the console contents based on the
 * multiline changes.
 */
public class ConsoleView implements PropertyChangeListener {
    private int startingRow, startingColumn;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    public void moveCursor(int rowDelta, int columnDelta) {

    }
}