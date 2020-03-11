public class AddCharAction implements Action {
    private char c;
    private String lineEnd;

    public AddCharAction(final char c, final String lineEnd) {
        this.c = c;
        this.lineEnd = lineEnd;
    }

    @Override
    public String actionString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(c);
        if (!lineEnd.isEmpty()) {
            stringBuilder.append("\033[s");
            stringBuilder.append(lineEnd);
            stringBuilder.append("\033[u");
        }
        return stringBuilder.toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AddCharAction(").append('c').append(", ").append(lineEnd).append(")");
        return stringBuilder.toString();
    }
}