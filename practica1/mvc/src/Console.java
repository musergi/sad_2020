import java.util.Observer;
import java.util.Observable;

public class Console implements Observer {
    public void update(Observable o, Object arg) {
        if (o instanceof Line) {
            Line line = (Line) o;
            System.out.print(line.getDisplayString());
        }
    }
}