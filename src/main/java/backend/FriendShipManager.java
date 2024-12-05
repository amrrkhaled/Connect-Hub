package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendShipManager implements IFriendShipManager{
    ILoadFriendShips loadFriendShips;
    IUserRepository userRepository;
    public FriendShipManager(ILoadFriendShips loadFriendShips , IUserRepository userRepository) {
        this.loadFriendShips = loadFriendShips;
        this.userRepository = userRepository;
    }
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
    // get requests for the user.
    public List<String> getFriendRequests(String userId) {
        return List.of();
    }
    // arrayList of friends that the user have with there status.
    @Override
    public List<String> getFriends(String userId) {
        List<String> friends = new ArrayList<>();
        String UserId , username , status;
        JSONArray friendships = loadFriendShips.loadFriendships();
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if (friendship.getString("userId1").equals(userId)) {
                UserId = friendship.getString("userId1");
            }
            if (friendship.getString("userId2").equals(userId)) {
                UserId = friendship.getString("userId1");
            }
          username = userRepository.getUsernameByUserId(userId);
          status = userRepository.getStatusByUserId(userId);
         //sameh(offline)
            friends.add(username + "(" + status + ")");
        }
        return friends;
    }
    // arrayList of friends of friends that user have.
    @Override
    public List<String> getFriendSuggestions(String userId) {
        return List.of();
    }
    // get pending that the user have sent.
    @Override
    public List<String> getPendingFriends(String userId) {
        return List.of();
    }

}
