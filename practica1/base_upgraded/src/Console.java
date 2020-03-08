import java.beans.*;

public class Console implements PropertyChangeListener{
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Action action = (Action) e.getNewValue();
        System.out.print(action.actionString());
    }
}
