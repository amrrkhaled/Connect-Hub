package backend.user;

import java.util.Collection;

public class User {
    public static String userId;

    public User(int i, String johnDoe, String mail) {
    }

    public static String getUserId() {
        return userId;
    }
    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public void addFriend(User friend) {
    }
}