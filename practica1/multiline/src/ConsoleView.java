import java.beans.*;

public class ConsoleView implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.print(evt.getNewValue());
    } 
}