import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConsoleView {
    private Map<String, Widget> widgets;
    private List<Widget> selectionList;
    private int selectionIndex;
    
    public ConsoleView() {
        widgets = new LinkedHashMap<>();
        selectionList = new ArrayList<>();
        selectionIndex = 0;
    }

	public void addWidget(Widget widget) {
        widgets.put(widget.getName(), widget);
        if (widget.isSelectable()) {
            selectionList.add(widget);
        }
        widget.setRoot(this);
    }
    
    public void onDraw() {
        System.out.print("\033[2J");
        for (Widget widget: widgets.values()) {
            widget.onDraw();
        }
        selectionList.get(selectionIndex).setCursor();
    }

    public void onUpdate(int key) {
        switch (key) {
            case 9:
                selectionIndex = (selectionIndex + 1) % selectionList.size();
                break;
            default:
                for (Widget widget: widgets.values()) {
                    widget.onUpdate(key);
                }
        }
    }

    public void onCleanUp() {
        System.out.print("\033[2J\033[1;1H");
    }

    public Widget getSelected() {
        return selectionList.get(selectionIndex);
    }

    public Widget getByName(String name) {
        return widgets.get(name);
    }

    public static abstract class Widget {
        protected int left, top;
        protected String name;
        protected ConsoleView root;

        public Widget(String name, int left, int top) {
            this.name = name;
            this.left = left;
            this.top = top;
        }

        public boolean isSelectable() {
            return false;
        }

        public void setCursor() {

        }

        public abstract void onDraw();
        public abstract void onUpdate(int key);
        public abstract String getValue();

        public void setRoot(ConsoleView root) {
            this.root = root;
        }

        public String getName() {
            return name;
        }
    }
}