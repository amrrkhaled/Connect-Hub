package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IFriendShipManager {
    public boolean RemoveFriendShip(String userId1, String userId2, JSONArray friendships);
    public JSONObject FindFriendShip(String userId1, String userId2, JSONArray friendships);
}
