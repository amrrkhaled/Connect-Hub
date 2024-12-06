package backend.friendship;

import backend.user.IUserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendshipService implements IFriendshipService {
    IUserRepository userRepository;
    ILoadFriendShips loadFriendShips;
    private  static FriendshipService instance;
    private FriendshipService(IUserRepository userRepository,ILoadFriendShips loadFriendShips) {
        this.userRepository = userRepository;
        this.loadFriendShips = loadFriendShips;

    }
    public static FriendshipService getInstance(IUserRepository userRepository,ILoadFriendShips loadFriendShips) {
        if (instance == null) {
            instance = new FriendshipService(userRepository,loadFriendShips);
        }
        return instance;
    }
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
    public List<String> getFriendsOfFriends(String FriendId, String myId) {
        List<String> oldFriendFriends = getFriends(myId);
        List<String> newFriendFriends = getFriends(FriendId);

        List<String> friendsOfFriends = new ArrayList<>();
        List<String> pendingFriends = getPendingFriends(myId);
        List<String> usernames = extractUsernames(pendingFriends);
        System.out.println("Usernames of pending friends:");
        for (String username : usernames) {
            usernames.
        }

        for (String friend : newFriendFriends) {
            if (!oldFriendFriends.contains(friend) && !friend.equals(myId) &&!friend.equals(FriendId) && !usernames.contains(friend)) {
                System.out.println(friend);
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
    public List<String> extractUsernames(List<String> pendingFriends) {
        List<String> usernames = new ArrayList<>();
        for (String friend : pendingFriends) {
            if (friend.endsWith("(pending)")) {
                usernames.add(friend.substring(0, friend.length() - "(pending)".length()));
            } else {
                usernames.add(friend);
            }
        }
        return usernames;
    }
    public List<String> getFriends(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        List<String> friends = new ArrayList<>();

        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);

            if (friendship.getString("status").equals("accepted") || friendship.getString("status").equals("blocked")) {
                if (friendship.getString("userId1").equals(userId)) {
                    friends.add(friendship.getString("userId2"));
                } else if (friendship.getString("userId2").equals(userId)) {
                    friends.add(friendship.getString("userId1"));
                }
            }
        }

        return friends;
    }
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



}
