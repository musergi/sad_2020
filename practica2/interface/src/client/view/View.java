package client.view;

public abstract class View {
    private String name;

    public View(String name) {
        this.name = name;
    }

    public abstract void onDraw();

    public void drawAt(String text, int column, int row) {
        System.out.print("\033[" + (row + 1) + ";" + (column + 1) + "H");
        System.out.print(text);
    }

    public String getName() {
        return name;
    }
}