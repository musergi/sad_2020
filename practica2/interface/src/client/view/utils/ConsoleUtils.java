package client.view.utils;

public class ConsoleUtils {
    public static native void init_console();
    public static native void set_raw_mode();
    public static native int get_console_width();
    public static native int get_console_height();
    public static native int get_cursor_column();
    public static native int get_cursor_row();
}