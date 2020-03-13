import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class TextView implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.print("\u001b[s");
        System.out.print(evt.getNewValue().toString());
        System.out.print("\u001b[u");
    }
}
