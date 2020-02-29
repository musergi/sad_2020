public class PrintWindowSize {
    public static void main(String[] args) throws Exception {
        System.out.print("\u001b[s\u001b[5000;5000H\u001b[6nu001b[u");
        System.out.println(System.in.read());
    }
}