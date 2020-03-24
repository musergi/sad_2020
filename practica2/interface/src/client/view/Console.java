package client.view;

import java.util.HashMap;
import java.util.Map;

public class Console {
    private View currentView;
    private Map<String, View> views;

    public Console() {
        views = new HashMap<>();

        View menuView = new MenuView(this);
        views.put(menuView.getName(), menuView);

    }

    public void onDraw() {
        System.out.print("\033[2J");
        currentView.onDraw();
    }

    public void setView(String viewName) {
        currentView = views.get(viewName);
        onDraw();
    }

    public String getView() {
        return currentView.getName();
    }
}