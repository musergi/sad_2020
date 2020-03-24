package server;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String name;
    private List<String> messages;

    public Chat(String user1, String user2) {
        name = genChatName(user1, user2);
        messages = new ArrayList<>();
    }

    public void send(String user, String message) {
        messages.add(user + " ~ " + message);
    }

    public static String genChatName(String user1, String user2) {
        if (user1.compareTo(user2) < 0) {
            return user1 + " " + user2;
        }
        return user2 + " " + user1;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}