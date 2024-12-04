package backend;

public class User {
    public static String userId;

    public static String getUserId() {
        return userId;
    }
    public static void setUserId(String userId) {
        User.userId = userId;
    }
}