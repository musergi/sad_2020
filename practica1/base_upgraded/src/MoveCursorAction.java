public class MoveCursorAction implements Action {
    private int delta;

    public MoveCursorAction(int delta) {
        this.delta = delta;
    }
    
    @Override
    public String actionString() {
        return (delta > 0) ? ("\033[" + delta + "C") : ("\033[" + Math.abs(delta) + "D");
    }
}