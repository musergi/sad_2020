import java.io.BufferedReader;
import java.util.*;

public class SequenceParser {
    private BufferedReader bufferedReader;

    public SequenceParser(final BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
    }

    /**
     * Reads the code corresponding to the pressed key parsing escape sequence if necessary
     * @return Integer representation of the pressed key
     */
    public int next() {
        bufferedReader.read();
    }

    /**
     * Leer un archivo de texto que tengan todas las secuencias y lo que devuelven (27-3-4--> -1)
     */
    private class Node {
        char inChar;
        List<Node> nodes;
        int value;

        public Node(char inChar, int value) {
            this.inChar = inChar;
            this.value = value;
            nodes = new ArrayList<>();
        }
    }
}

