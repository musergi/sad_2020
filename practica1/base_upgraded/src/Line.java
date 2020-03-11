import java.beans.*;
import java.util.*;

public class Line {
    private StringBuilder stringBuilder;
    private int cursor;
    private boolean insert;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Line default constructor, inits empty StringBuilder and intantiates
     * a new Console view object.
     */
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
        // Delete right character if in insert mode
        if (insert){
            delete();
        }
        // Add new character in the cursor position
        stringBuilder.insert(cursor, c);
        cursor++;

        // Signal view
        String lineEnd = stringBuilder.substring(cursor - 1);
        pcs.firePropertyChange("charbuffer", null, new AddCharAction(lineEnd));
    }

    /**
     * Remove the character to the left of the cursor
     */
    public void backspace(){
        // Check if the cursor has a character to the left
        if (cursor == 0){
            // TODO: Play error sound
            return;
        }
        // Remove charcter to the left of the cursor and move cursor to new position
        stringBuilder.deleteCharAt(cursor - 1);
        cursor--;

        // Signal view
        String lineEnd = stringBuilder.substring(cursor);
        pcs.firePropertyChange("charbuffer", null, new RemoveCharAction(false, lineEnd));
    }

    /**
     * Removes the cursors current character
     */
    public void delete(){
        // Check if cursor is at the end of the string and has no charactert in its position
        if (cursor == stringBuilder.length()){
            // TODO: Play error sound
            return;
        }
        // Remove character, no need to update cursor position
        stringBuilder.deleteCharAt(cursor);

        // Signal view
        String lineEnd = stringBuilder.substring(cursor);
        pcs.firePropertyChange("charbuffer", null, new RemoveCharAction(true, lineEnd));
    }

    /**
     * Moves the cursor the spefied positions
     * @param delta Integer representing the number of character to move to the
     * right, negative integers are used to represent right movement
     */
    public void moveCursor(int delta){
        // Save initial cursor state
        int startingCursor = cursor;

        // Update cursor position bound to 0 and buffer length included
        cursor = Math.min(Math.max(cursor + delta, 0), stringBuilder.length());

        // Signal view
        int trueCursorDelta = cursor - startingCursor;
        if (trueCursorDelta != 0) {
            pcs.firePropertyChange("cursor", null, new MoveCursorAction(trueCursorDelta));
        }
    }

    /**
     * Moves the cursor to the start of the line
     */
    public void home(){
        // Save initial cursor state
        int startingCursor = cursor;

        // Move cursor to start
        cursor  = 0;

        // Signal view
        pcs.firePropertyChange("cursor", null, new MoveCursorAction(cursor - startingCursor));
    }

    /**
     * Moves the cursor to the end of the line
     */
    public void end(){
        // Save initial cursor state
        int startingCursor = cursor;

        // Move cursor to last position in the line
        cursor = stringBuilder.length();

        // Signal view
        pcs.firePropertyChange("cursor", null, new MoveCursorAction(cursor - startingCursor));
    }

    /**
     * Toggles the insertion mode
     */
    public void toggleInsert(){
        insert = !insert;
    }

    /**
     * Returns the final entered string, ready to be outputed.
     */
    public String toString(){
        return stringBuilder.toString();
    }
}
