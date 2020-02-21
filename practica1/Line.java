import java.util.*;

public class Line {
    private List<Integer> rawInput;

    public Line() {
        rawInput = new ArrayList<>();
    }

    public void addInput(int code) {
        rawInput.add(code);
    }

    public String toString() {
        return Arrays.toString(rawInput.toArray());
    }
}