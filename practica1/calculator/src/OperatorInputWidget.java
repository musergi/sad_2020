public class OperatorInputWidget extends InputWidget {
    public OperatorInputWidget(String name, String caption, int left, int top) {
        super(name, caption, left, top);
    }

    @Override
    public void onUpdate(int key) {
        if (root.getSelected() == this) {
            if ("+-*/".contains(Character.toString((char) key))) {
                if (content.length() == 0) {
                    content.append((char) key);
                } else {
                    content.setCharAt(0, (char) key);
                }
            }
        }
    }
}