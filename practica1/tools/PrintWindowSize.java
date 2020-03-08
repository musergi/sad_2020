public class PrintWindowSize {
    public static void main(String[] args) throws Exception {
        System.out.print("\u001b[18");
        int read = 0;
        while ((read = System.in.read()) != 27) {
            System.out.println(read);
        }
    }
}