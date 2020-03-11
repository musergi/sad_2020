import java.beans.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console implements PropertyChangeListener{
    public static final Logger LOGGER = Logger.getLogger("logger");

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Action action = (Action) e.getNewValue();
        LOGGER.log(Level.ALL, action.toString());
        System.out.print(action.actionString());
    }
}
