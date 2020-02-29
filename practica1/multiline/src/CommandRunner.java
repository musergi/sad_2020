import java.io.*;

public class CommandRunner {
    public static String run(String cmd) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", cmd);
        try {
            Process process = processBuilder.start();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                    process.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }

            int exitValue = process.waitFor();
            if (exitValue != 0) {
                throw new IOException("Failed to run command: " + cmd);
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
} 