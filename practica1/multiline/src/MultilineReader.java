import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MultilineReader extends BufferedReader {
    private SequenceParser parser;
    private Multiline multiline;

    public MultilineReader(Reader in) {
        super(in);
        parser = new SequenceParser(new BufferedReader(in));
        multiline = new Multiline();
    }
    
    public int read() throws IOException {
        return parser.next();
    }

    public String readLines() throws IOException {
        int keyCode;
        ConsoleUtils.set_raw_mode();
        while ((keyCode = read()) != SequenceParser.K_CTRL_D) {
            multiline.process(keyCode);
            System.out.println(multiline);
        }
        return multiline.getFinalString();
    }
}