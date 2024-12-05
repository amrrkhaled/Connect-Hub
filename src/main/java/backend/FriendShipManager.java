package backend;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class FriendShipManager implements IFriendShipManager {
    ILoadFriendShips loadFriendShips;
    IUserRepository userRepository;

    public FriendShipManager(ILoadFriendShips loadFriendShips, IUserRepository userRepository) {
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
    public JSONObject FindFriendShip(String userId1, String userId2, JSONArray friendships) {
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
        JSONArray friendships = loadFriendShips.loadFriendships();
        List<String> friendRequests = new ArrayList<>();
        String SenderId, username;
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if (friendship.getString("userId2").equals(userId) && friendship.getString("status").equals("pending")) {
                SenderId = friendship.getString("userId1");
                username = userRepository.getUsernameByUserId(SenderId);
                // ahmed
                friendRequests.add(username);
            }
        }
        return friendRequests;
    }

    // arrayList of friends that the user have with there status.
    @Override
    public List<String> getFriendsWithStatus(String userId) {
        List<String> friends = new ArrayList<>();
        JSONArray friendships = loadFriendShips.loadFriendships();

        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            String friendId = null;
            if (friendship.getString("userId1").equals(userId) && friendship.getString("status").equals("accepted")) {
                friendId = friendship.getString("userId2");
            }
            if (friendship.getString("userId2").equals(userId) && friendship.getString("status").equals("accepted")) {
                friendId = friendship.getString("userId1");
            }
            if (friendId != null) {
                String username = userRepository.getUsernameByUserId(friendId);
                String status = userRepository.getStatusByUserId(friendId);
                // Add friend in the format "ahmed(online)"
                friends.add(username + "(" + status + ")");
            }
        }
        return friends;
    }
    // arrayList of friends of friends that user have.
    @Override
    public List<String> getFriendSuggestions(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        Set<String> friendSuggestions = new HashSet<>(); // Use Set to avoid duplicates
        List<String> userFriends = getFriends(userId);
        for (String friend : userFriends) {
            // Find mutual friends who are not already direct friends
            List<String> friendsOfFriends = getFriendsOfFriends(friend, userId);
            friendSuggestions.addAll(friendsOfFriends);
        }

        return new ArrayList<>(friendSuggestions);
    }
    public List<String> getFriendsOfFriends(String oldFriendId, String newFriendId) {
        List<String> oldFriendFriends = getFriends(oldFriendId);
        List<String> newFriendFriends = getFriends(newFriendId);
        List<String> friendsOfFriends = new ArrayList<>();
        for (String friend : oldFriendFriends) {
            if (!newFriendFriends.contains(friend) && !friend.equals(newFriendId)) {
                friendsOfFriends.add(friend);
            }
        }
        for (int i = 0; i < friendsOfFriends.size(); i++) {
            String friendId = friendsOfFriends.get(i);
            String username = userRepository.getUsernameByUserId(friendId);
            friendsOfFriends.set(i, username);
        }
        return friendsOfFriends;
    }

    public List<String> getFriends(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        List<String> friends = new ArrayList<>();

        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);

            if (friendship.getString("status").equals("accepted")) {
                if (friendship.getString("userId1").equals(userId)) {
                    friends.add(friendship.getString("userId2"));
                } else if (friendship.getString("userId2").equals(userId)) {
                    friends.add(friendship.getString("userId1"));
                }
            }
        }

        return friends;
    }

    // get pending that the user have sent.
    @Override
    public List<String> getPendingFriends(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        List<String> pendingFriends = new ArrayList<>();
        String ReceiverId, username;
        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);
            if (friendship.getString("userId1").equals(userId) && friendship.getString("status").equals("pending")) {
                ReceiverId = friendship.getString("userId2");
                username = userRepository.getUsernameByUserId(ReceiverId);
                //saeed(pending)
                pendingFriends.add(username + "(pending)");
            }
        }
        return pendingFriends;
    }
}
