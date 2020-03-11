import java.beans.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console implements PropertyChangeListener{
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Action action = (Action) e.getNewValue();
        System.out.print(action.actionString());
    }
}
