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
        // Get the current line
        StringBuilder currentLine = lines.get(cursorRow);
        
        
        if (insert && cursorColumn < currentLine.length()) {
            // If in insert mode and a char is in the cursor position
            
            // Replace cursor char by incoming char
            currentLine.setCharAt(cursorColumn, c);
        } else {
            // Append char in specified position
            currentLine.insert(cursorColumn, c);
        }
        
        // Update text in view
        pcs.firePropertyChange("text", null, currentLine.substring(cursorColumn));
        
        // Move cursor forward by 1
        cursorColumn++;
        
        // Update cursor view
        pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(0, 1));
    }

    public void moveCursorH(int delta) {
        // Save cursor position for view update
        int previousCursorRow = cursorRow;
        int previousCursorColumn = cursorColumn;
    
        // Calculate new cursor position
        int newCursorColumn = cursorColumn + delta;
        
        if (newCursorColumn == -1 && cursorRow > 0) {
            // If the cursor is alredy at the start and has a line above
            
            // Go up one line
            cursorRow--;
            // Go to the end of the previous line
            cursorColumn = lines.get(cursorRow).length();
        } else if (newCursorColumn == lines.get(cursorRow).length() + 1 && cursorRow < lines.size() - 1) {
            // If the cursor is at the end of the line and has a line above
            
            // Move to the start of the next row
            cursorRow++;
            cursorColumn = 0;
        } else if (newCursorColumn >= 0 && newCursorColumn <= lines.get(cursorRow).length()) {
            // If the new cursor column is in the current line boundries
            
            // Update cursor column
            cursorColumn = newCursorColumn;
        }
        
        // Update view
        pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(
            cursorRow - previousCursorRow,
            cursorColumn - previousCursorColumn));
    }

    public void moveCursorV(int delta) {
        // Save cursor position for view update
        int previousCursorRow = cursorRow;
        int previousCursorColumn = cursorColumn; 
    
        // Move cursor verticaly
        cursorRow = Math.min(Math.max(0, cursorRow + delta), lines.size() - 1);
        
        // If new line is shorter move cursor to line end
        cursorColumn = Math.min(cursorColumn, lines.get(cursorRow).length());
        
        // Update view
        pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(
            cursorRow - previousCursorRow,
            cursorColumn - previousCursorColumn));
    }

    public void delete(boolean right) {
        // Get current line
        StringBuilder currentLine =  lines.get(cursorRow);
        
        // Calculate deletion position
        int deletePosition = right ? cursorColumn : cursorColumn - 1;
        if (deletePosition >= 0 && deletePosition < currentLine.length()) {
            // If in boundries
            
            // Move cursor if necesary
            moveCursorH(right ? 0 : -1);
            
            // Delte proper character
            currentLine.deleteCharAt(deletePosition);
            
            // Update line end to fit new text
            pcs.firePropertyChange("text", null, "\u001b[K" + currentLine.substring(cursorColumn));
        } else if (deletePosition == -1 && cursorRow > 0) {
            // If at the start of row and has a row above

            // Get previous line
            StringBuilder previousLine = lines.get(cursorRow - 1);
            
            // Remove line
            lines.remove(cursorRow);
            // Update view to line removal
            String[] movedLines = new String[lines.size() - cursorRow];
            for (int i = 0; i < movedLines.length; i++) {
                movedLines[i] = lines.get(cursorRow + i).toString();
            }
            pcs.firePropertyChange("text", null, "\u001b[J" + String.join("\n", movedLines));
            
            // Move cursor to the end of the previous line
            cursorColumn = previousLine.length();
            cursorRow--;
            // Update view
            pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(-1, cursorColumn));
            
            // Add deleted line to the end of the line
            previousLine.append(currentLine);
            pcs.firePropertyChange("text", null, currentLine.toString());
        } else if (deletePosition == currentLine.length() && cursorRow < lines.size() - 1) {
            // If delete is pressed at the end of the line
            
            // Get line to be removed
            StringBuilder removeLine = lines.get(cursorRow + 1);
            lines.remove(cursorRow + 1);
            // Update view
            String[] movedLines = new String[lines.size() - cursorRow - 1];
            for (int i = 0; i < movedLines.length; i++) {
                movedLines[i] = lines.get(cursorRow + i + 1).toString();
            }
            pcs.firePropertyChange("text", null, "\n\u001b[J" + String.join("\n", movedLines));
            
            // Add removed line to the end of current line
            currentLine.append(removeLine);
            // Update view
            pcs.firePropertyChange("text", null, removeLine.toString());
        }
    }

    public void lineJump() {
        // Get current line
        StringBuilder currentLine = lines.get(cursorRow);
        String carriedText = currentLine.substring(cursorColumn);
        
        // Clear current line
        currentLine.delete(cursorColumn, currentLine.length());
        pcs.firePropertyChange("text", null, "\u001b[K");
        
        // Build new line
        StringBuilder newLine = new StringBuilder();
        newLine.append(carriedText);

        // Move cursor to new line
        int previousColumn = cursorColumn;
        cursorRow++;
        cursorColumn = 0;
        pcs.firePropertyChange("cursor", null, new CursorView.CursorDelta(1, -previousColumn));
        
        // Add new line
        lines.add(cursorRow, newLine);
        
        // Send changed lines to terminal
        String[] movedLines = new String[lines.size() - cursorRow];
        for (int i = 0; i < movedLines.length; i++) {
            movedLines[i] = "\u001b[K" + lines.get(cursorRow + i).toString();
        }
        pcs.firePropertyChange("text", null, String.join("\n", movedLines));
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
