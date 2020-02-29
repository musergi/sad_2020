import java.beans.PropertyChangeSupport;

public class MultiLine {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public MultiLine(ConsoleView consoleView) {
        propertyChangeSupport.addPropertyChangeListener(consoleView);
    }
}