import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintInputCodes {
    public static void main(String[] args) {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();
            List<Integer> codes = new ArrayList<>();
            int code = 0;
            while ((code = System.in.read()) != 13) {
                codes.add(code);
                System.out.println(Arrays.toString(codes.toArray()));
            }
            cmd[2] = "stty echo cooked </dev/tty";
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}