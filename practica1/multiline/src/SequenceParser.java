import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class SequenceParser {
    public static final int K_RETURN = 13;
    public static final int K_BACKSPACE = 127;

    public static final int K_LEFT = -2;
    public static final int K_RIGHT = -3;
    public static final int K_UP = -4;
    public static final int K_DOWN = -5;
    public static final int K_HOME = -6;
    public static final int K_END = -7;
    public static final int K_INSERT = -8;
    public static final int K_DELETE = -9;
    public static final int K_PG_UP = -10;
    public static final int K_PG_DOWN = -11;

    public static final int K_F0 = -20;
    public static final int K_F1 = -21;
    public static final int K_F2 = -22;
    public static final int K_F3 = -23;
    public static final int K_F4 = -24;
    public static final int K_F5 = -25;
    public static final int K_F6 = -26;
    public static final int K_F7 = -27;
    public static final int K_F8 = -28;
    public static final int K_F9 = -29;
    public static final int K_F10 = -30;
    public static final int K_F11 = -31;
    public static final int K_F12 = -32;

    public static final int K_CTRL_D = 4;

    private BufferedReader bufferedReader;
    private Node currentNode;
    private Node startingNode;

    public SequenceParser(final BufferedReader bufferedReader) {
        // Asign buffer to read
        this.bufferedReader = bufferedReader;

        // Create tree
        startingNode = new Node();
        // xterm sequences
        addSequence("\u001b[A", K_DOWN);
        addSequence("\u001b[B", K_UP);
        addSequence("\u001b[C", K_RIGHT);
        addSequence("\u001b[D", K_LEFT);
        addSequence("\u001b[F", K_END);
        addSequence("\u001b[H", K_HOME);
        addSequence("\u001b[1P", K_F1);
        addSequence("\u001b[1Q", K_F2);
        addSequence("\u001b[1R", K_F3);
        addSequence("\u001b[1S", K_F4);
        // vt sequences
        addSequence("\u001b[1~", K_HOME);
        addSequence("\u001b[2~", K_INSERT);
        addSequence("\u001b[3~", K_DELETE);
        addSequence("\u001b[4~", K_END);
        addSequence("\u001b[5~", K_PG_UP);
        addSequence("\u001b[6~", K_PG_DOWN);
        addSequence("\u001b[7~", K_HOME);
        addSequence("\u001b[8~", K_END);
        addSequence("\u001b[10~", K_F0);
        addSequence("\u001b[11~", K_F1);
        addSequence("\u001b[12~", K_F2);
        addSequence("\u001b[13~", K_F3);
        addSequence("\u001b[14~", K_F4);
        addSequence("\u001b[15~", K_F5);
        addSequence("\u001b[17~", K_F6);
        addSequence("\u001b[18~", K_F7);
        addSequence("\u001b[19~", K_F8);
        addSequence("\u001b[20~", K_F9);
        addSequence("\u001b[21~", K_F10);
        addSequence("\u001b[23~", K_F11);
        addSequence("\u001b[24~", K_F12);

        // Set current node to the starting node
        currentNode = startingNode;
    }

    /**
     * Adds the required nodes so the specified sequence can be detected
     * and the code returned
     * @param sequence sequence to be added
     * @param code code returned when the sequence is received
     */
    private void addSequence(String sequence, int code) {
        // Get a pointer to the starting node
        Node pointer = startingNode;

        // Go down the tree adding nodes when necessary
        for (int i = 0; i < sequence.length(); i++) {
            // Get next char
            int inChar = (int) sequence.charAt(i);

            // Add branch if does not exist
            if (!pointer.hasBranch(inChar)) {
                // Add value if last node
                Node newNode = new Node(inChar, i == sequence.length() - 1 ? code : 0);
                pointer.addBranch(newNode);
            }

            // Move to branch so next node can start on branch
            pointer = pointer.getBranch(inChar);
        }
    }

    /**
     * Reads the code corresponding to the pressed key parsing escape sequence if
     * necessary
     * 
     * @return Integer representation of the pressed key
     * @throws IOException
     */
    public int next() throws IOException {
        // Variable to put the read keycode
        int keyCode = 0;

        // Read until full expresion is read
        do {
            // Read key
            keyCode = bufferedReader.read();

            // If possible go to child node
            if (currentNode.hasBranch(keyCode)) {
                currentNode = currentNode.getBranch(keyCode);
            }
        } while(currentNode != startingNode && !currentNode.isEnd());

        // Get value if end node, keyCode if startingNode
        int value = currentNode == startingNode ? keyCode : currentNode.getValue();

        // Rewind and return
        currentNode = startingNode;
        return value;
    }

    /**
     * Leer un archivo de texto que tengan todas las secuencias y lo que devuelven (27-3-4--> -1)
     */
    private class Node {
        private int inChar;
        private Map<Integer,Node> nodes;
        private int value;

        /**
         * Creates a node with the specified values
         * @param inChar value to get to the state
         * @param value returned value if final node
         */
        public Node(int inChar, int value) {
            this.inChar = inChar;
            this.value = value;
            nodes = new HashMap<>();
        }

        /**
         * Creates a node with no return value
         * @param inChar value to get to the state
         */
        public Node(int inChar) {
            this(inChar, 0);
        }

        /**
         * Creates starting node
         */
        public Node() {
            this(0);
        }

        /**
         * Adds the node to the possible transitions
         * @param node node to connect
         */
        public void addBranch(Node node) {
            nodes.put(node.getInChar(), node);
        }

        /**
         * @param keyCode The keyCode of the next branch
         * @return The next branch available connected to the keyCode, 
         * if not available returns currentNode
         */
        public Node getBranch(int keyCode){
            return nodes.get(keyCode);
        }

        /**
         * Checks if node has a matching branch
         * @param keyCode in character for branch
         * @return if the node has the specified branch
         */
        public boolean hasBranch(int keyCode){
            return nodes.containsKey(keyCode);
        }

        /**
         * Checks if its a final node
         * @return true if has no branches
         */
        public boolean isEnd() {
            return nodes.isEmpty();
        }

        public int getInChar(){
            return inChar;
        }

        public int getValue(){
           return value;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Node(");
            stringBuilder.append(inChar).append(",").append(value);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }
}

