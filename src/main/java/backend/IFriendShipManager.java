package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IFriendShipManager {
    public boolean RemoveFriendShip(String userId1, String userId2, JSONArray friendships);
    public JSONObject FindFriendShip(String userId1, String userId2, JSONArray friendships);
    public List<String> getFriendsWithStatus(String userId);
    public List<String> getFriends(String userId);
    public List<String> getFriendsOfFriends(String oldFriendId, String newFriendId);
    public List<String> getPendingFriends(String userId);
    public List<String> getFriendSuggestions(String userId);
    public List<String> getFriendRequests(String userId);


}
