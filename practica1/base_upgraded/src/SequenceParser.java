import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class SequenceParser {
    private BufferedReader bufferedReader;
    private Node currentNode;
    private Node startingNode;

    public SequenceParser(final BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
        startingNode = new Node(0, 0);
        currentNode = startingNode;
        Node node27 = new Node(27, 0);
        Node node91 = new Node(91, 0);
        Node node67 = new Node(67, -2);
        startingNode.addBranch(node27);
        node27.addBranch(node91);
        node91.addBranch(node67);
    }

    /**
     * Reads the code corresponding to the pressed key parsing escape sequence if
     * necessary
     * 
     * @return Integer representation of the pressed key
     * @throws IOException
     */
    public int next() throws IOException {
        int keyCode = 0;
        do {
            keyCode = bufferedReader.read();
            currentNode = currentNode.getBranch(keyCode);
        } while(currentNode.hasBranch(keyCode));
        int value = currentNode.getValue();
        currentNode = startingNode;
        return value;
    }

    /**
     * Leer un archivo de texto que tengan todas las secuencias y lo que devuelven (27-3-4--> -1)
     */
    private class Node {
        int inChar;
        Map<Integer,Node> nodes;
        int value;
        /**
         * @param inChar Value to get to the state
         * @param value The returned value if arrived to the final node
         */
        public Node(int inChar, int value) {
            this.inChar = inChar;
            this.value = value;
            nodes = new HashMap<>();
        }

        public void addBranch(Node node) {
            nodes.put(node.getInChar(), node);
        }

        /**
         * @param keyCode The keyCode of the next branch
         * @return The next branch available connected to the keyCode, 
         * if not available returns currentNode
         */
        public Node getBranch(int keyCode){
            if (hasBranch(keyCode)){
                return nodes.get(keyCode);
            }
            return this;
        }

        public boolean hasBranch(int keyCode){
            return nodes.containsKey(keyCode);
        }

        public int getInChar(){
            return inChar;
        }

        public int getValue(){
           return value;
        }
    }
}

