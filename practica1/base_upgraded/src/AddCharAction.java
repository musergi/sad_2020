public class AddCharAction implements Action {
    private String lineEnd;

    public AddCharAction(final String lineEnd) {
        this.lineEnd = lineEnd;
    }

    @Override
    public String actionString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lineEnd);
        if (lineEnd.length() > 1) {
            stringBuilder.append("\033[").append(lineEnd.length() - 1).append("D");
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AddCharAction(").append(lineEnd).append(")");
        return stringBuilder.toString();
    }
}