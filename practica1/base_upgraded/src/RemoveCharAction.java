public class RemoveCharAction implements Action {
    private boolean isRight;
    private String lineEnd;
    
    public RemoveCharAction(boolean isRight, String lineEnd) {
        this.isRight = isRight;
        this.lineEnd = lineEnd;
    }

    @Override
    public String actionString() {
        StringBuilder stringBuilder = new StringBuilder();
        
        // Move left if not right remove
        if (!isRight) {
            stringBuilder.append("\033[1D");
        }
        stringBuilder.append("\033[s");
        stringBuilder.append(lineEnd).append(" ");
        stringBuilder.append("\033[u");
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RemoveCharAction(").append(isRight).append(", ").append(lineEnd).append(")");
        return stringBuilder.toString();
    }
}