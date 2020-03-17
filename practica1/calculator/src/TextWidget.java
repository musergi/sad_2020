public class TextWidget extends ConsoleView.Widget {
    protected String content;

    public TextWidget(String name, String startingText, int left, int top) {
        super(name, left, top);
        content = startingText;
    }

    public TextWidget(String name, int left, int top) {
        super(name, left, top);
        content = "";
    }
    
    @Override
    public void onDraw() {
        System.out.print("\033[" + (top + 1) + ";" + (left + 1) + "H");
        System.out.print(content);

    }

    @Override
    public void onUpdate(int key) {

    }

    @Override
    public String getValue() {
        return content;
    }
}