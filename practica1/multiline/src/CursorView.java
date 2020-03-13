import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class CursorView implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof CursorDelta ) {
            CursorDelta cursorDelta = (CursorDelta) newValue;
            System.out.print(cursorDelta.getAnsiEscape());
        }
    }
    
    public static class CursorDelta {
        private int row, column;
        
        public CursorDelta(int row, int column) {
            this.row = row;
            this.column = column;
        }
        
        public String getAnsiEscape() {
            StringBuilder stringBuilder = new StringBuilder();
            if (row != 0) {
                stringBuilder.append("\u001b[");
                stringBuilder.append(Math.abs(row));
                stringBuilder.append(row > 0 ? 'B' : 'A');
            }
            if (column != 0) {
                stringBuilder.append("\u001b[");
                stringBuilder.append(Math.abs(column));
                stringBuilder.append(column > 0 ? 'C': 'D');
            }
            return stringBuilder.toString();
        }
    }
}
