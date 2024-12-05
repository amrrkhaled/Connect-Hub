package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class FriendShipManager implements IFriendShipManager{
    @Override
    public boolean RemoveFriendShip(String userId1, String userId2, JSONArray friendships) {
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if ((friendship.getString("userId1").equals(userId1) && friendship.getString("userId2").equals(userId2)) ||
                    (friendship.getString("userId1").equals(userId2) && friendship.getString("userId2").equals(userId1))) {
                friendships.remove(i);
                return true;
            }
        }
        return false;
    }
    @Override
    public JSONObject FindFriendShip(String userId1, String userId2, JSONArray friendships){
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if ((friendship.getString("userId1").equals(userId1) && friendship.getString("userId2").equals(userId2)) ||
                    (friendship.getString("userId1").equals(userId2) && friendship.getString("userId2").equals(userId1))) {
                return friendship;
            }
        }
        return null;
    }

    @Override
    public List<String> getFriendRequests(String userId) {
        return List.of();
    }

    @Override
    public List<String> getFriends(String userId) {
        return List.of();
    }

    @Override
    public List<String> getFriendSuggestions(String userId) {
        return List.of();
    }

    @Override
    public List<String> getPendingFriends(String userId) {
        return List.of();
    }
}
