package client.view;

public class MenuView extends View {
    public MenuView(Console console) {
        super("Menu");
    }

    @Override
    public void onDraw() {
        drawAt("1 - Open chat", 2, 1);
        drawAt("2 - Exit", 2, 2);
        System.out.print("\033[?25l");
    }
}