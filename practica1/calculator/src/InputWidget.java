public abstract class InputWidget extends ConsoleView.Widget {
    public static final int DEFAULT_WIDTH = 14;
    private String caption;
    protected StringBuilder content;

    public InputWidget(String name, String caption, int left, int top) {
        super(name, left, top);
        this.caption = caption;
        content = new StringBuilder();
    }

    @Override
    public void setCursor() {
        System.out.print("\033[" + (top + 1) + ";" + (left + caption.length() + content.length() + 1) + "H");
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public void onDraw() {
        System.out.print("\033[" + (top + 1) + ";" + (left + 1) + "H");
        System.out.print(caption);
        System.out.print(content);
    }

    @Override
    public String getValue() {
        return content.toString();
    }

    @Override
    public abstract void onUpdate(int key);
}