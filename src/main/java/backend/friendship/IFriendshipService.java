package backend.friendship;

import java.util.List;

import org.json.*;

public interface IFriendshipService {
    public List<String> getFriends(String userId);

    public List<String> getFriendsOfFriends(String friendId, String myId);

    public List<String> getFriendsWithStatus(String userId);

    public boolean RemoveFriendShip(String userId1, String userId2, JSONArray friendships);

    public JSONObject FindFriendShip(String userId1, String userId2, JSONArray friendships);

    public List<String> getPendingFriends(String userId);

    public List<String> extractUsernames(List<String> pendingFriends);

    public List<String> getBlockedFriends(String userId);

    public boolean areTheyFriends(String userId1, String userId2);

    public boolean areTheyPending(String userId1, String userId2);

}
