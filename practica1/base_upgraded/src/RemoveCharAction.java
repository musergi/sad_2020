public class RemoveCharAction implements Action {
    private boolean isRight;
    private String lineEnd;
    
    public RemoveCharAction(boolean isRight, String lineEnd) {
        this.isRight = isRight;
        this.lineEnd = lineEnd;
    }

    @Override
    public String actionString() {
        return (isRight ? "" : "\033[1D") + lineEnd + " " + "\033[" + (lineEnd.length() + 1) + "D";
    }
}