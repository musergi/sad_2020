public class RemoveCharAction implements Action {
    public boolean isRight;
    
    public RemoveCharAction(boolean isRight) {
        this.isRight = isRight;
    }

    @Override
    public String actionString() {
        return (isRight ? "\033[1C" : "") + "\b";
    }
}