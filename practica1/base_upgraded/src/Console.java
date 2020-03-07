import java.beans.*;

public class Console implements PropertyChangeListener{
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Object propertyName = e.getNewValue();
        System.out.print(propertyName.toString());
    }
}