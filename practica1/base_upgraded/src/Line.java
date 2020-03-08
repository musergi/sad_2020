import java.beans.*;
import java.util.*;

public class Line {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Line(){
        stringBuilder = new StringBuilder();
        pcs.addPropertyChangeListener(new Console());
    }

    /**
     * Adds the specified character to the line at the correct position depending
     * on internal state.
     * @param c char to be inserted to the line
     */
    public void addChar(char c){
        if (insert){
            delete();
        }
        stringBuilder.insert(cursor, c);
        cursor++;
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Remove the character to the left of the cursor
     */
    public void backspace(){
        if (cursor == 0){
            return;
        }
        stringBuilder.deleteCharAt(cursor-1);
        cursor--;
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Removes the cursors current character
     */
    public void delete(){
        if (cursor == stringBuilder.length()){
            return;
        }
        stringBuilder.deleteCharAt(cursor);
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Moves the cursor the spefied positions
     * @param delta Integer representing the number of character to move to the
     * right, negative integers are used to represent right movement
     */
    public void moveCursor(int delta){
        cursor = Math.min(Math.max(cursor + delta, 0), stringBuilder.length());
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Moves the cursor to the start of the line
     */
    public void home(){
        cursor  = 0;
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Moves the cursor to the end of the line
     */
    public void end(){
        cursor = stringBuilder.length();
        pcs.firePropertyChange("display", null, action());
    }

    /**
     * Toggles the insertion mode
     */
    public void toggleInsert(){
        insert = !insert;
    }

    /**
     * Deprecated
     * @return
     */
    public String action(){
        StringBuilder commands = new StringBuilder();
        commands.append('\r');
        commands.append(stringBuilder.toString());
        commands.append(" ");
        commands.append("\033[");
        commands.append(stringBuilder.length() - cursor + 1);
        commands.append("D");
        return commands.toString();
    }

    /**
     * Returns the final entered string, ready to be outputed.
     */
    public String toString(){
        return stringBuilder.toString();
    }
}