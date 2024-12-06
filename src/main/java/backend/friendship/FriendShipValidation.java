package backend.friendship;

import org.json.JSONArray;
import org.json.JSONObject;

public class FriendShipValidation implements IFriendShipValidation {

    // Private static instance of the class
    private static FriendShipValidation instance;

    // Private constructor to prevent instantiation
    private FriendShipValidation() {
    }

    // Public method to get the singleton instance
    public static synchronized FriendShipValidation getInstance() {
        if (instance == null) {
            instance = new FriendShipValidation();
        }
        return instance;
    }

    // Method to check for duplicates in friendships
    public boolean checkDuplicates(String userId1, String userId2, JSONArray friendships) {
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if ((friendship.getString("userId1").equals(userId1) && friendship.getString("userId2").equals(userId2)) ||
                    (friendship.getString("userId1").equals(userId2) && friendship.getString("userId2").equals(userId1))) {
                System.out.println("Friendship already exists!");
                return true;
            }
        }
        return false;
    }
}

