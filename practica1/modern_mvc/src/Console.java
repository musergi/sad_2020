import java.beans.*;

public class Console implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof String) {
            System.out.print((String) newValue);
        }
    }
}