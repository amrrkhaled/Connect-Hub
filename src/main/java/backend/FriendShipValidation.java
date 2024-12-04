package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public class FriendShipValidation implements IFriendShipValidation {
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
