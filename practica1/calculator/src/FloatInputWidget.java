public class FloatInputWidget extends InputWidget {
    public FloatInputWidget(String name, String caption, int left, int top) {
        super(name, caption, left, top);
    }

    @Override
    public void onUpdate(int key) {
        if (root.getSelected() == this) {
            if (key >= '0' && key <= '9') {
                content.append((char) key);
            } else if (key == 127 && content.length() > 0) {
                content.deleteCharAt(content.length() - 1);
            } else if (key == '.' && !content.toString().contains(".")) {
                content.append('.');
            }
        }
    }
}