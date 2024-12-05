package backend.friendship;

import backend.user.ILoadUsers;
import backend.user.IUserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


public class FriendShipManager implements IFriendShipManager {
    ILoadFriendShips loadFriendShips;
    IUserRepository userRepository;
    ILoadUsers loadUsers;
    // for singelton designPattern
    private static FriendShipManager instance;
    // Private constructor to prevent instantiation from outside
    private FriendShipManager(ILoadFriendShips loadFriendShips, IUserRepository userRepository, ILoadUsers loadUsers) {
        // Initialization logic here
        this.loadFriendShips = loadFriendShips;
        this.userRepository = userRepository;
        this.loadUsers = loadUsers;
    }
    // Public method to access the single instance
    public static synchronized FriendShipManager getInstance(ILoadFriendShips loadFriendShips, IUserRepository userRepository, ILoadUsers loadUsers) {
        if (instance == null) {
            instance = new FriendShipManager(loadFriendShips, userRepository, loadUsers);
        }
        return instance;
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
        if(userFriends.size() > 0) {
            for (String friend : userFriends) {
                // Find mutual friends who are not already direct friends
                List<String> friendsOfFriends = getFriendsOfFriends(friend, userId);
                friendSuggestions.addAll(friendsOfFriends);
            }
        }
        else{
            List<String> pendingFriends=getPendingFriends(userId);
            List<String> usernames = extractUsernames(pendingFriends);
            List<String> friendRequests = getFriendRequests(userId);
            JSONArray users = loadUsers.loadUsers();
            for (int i = 0; i < users.length(); i++) {
                JSONObject userJson = users.getJSONObject(i);
                String suggestion = userJson.getString("username");
                if(!usernames.contains(suggestion) && !friendRequests.contains(suggestion)) {
                    friendSuggestions.add(suggestion);
//                    System.out.println(suggestion);
//                    System.out.println(usernames);
                }
            }
        }
            return new ArrayList<>(friendSuggestions);
    }
    public String extractUsername(String friendUsernameWithStatus) {  if (friendUsernameWithStatus != null) {
        // Check if the username contains a status in parentheses
        int statusStartIndex = friendUsernameWithStatus.lastIndexOf("(");
        int statusEndIndex = friendUsernameWithStatus.lastIndexOf(")");

        if (statusStartIndex != -1 && statusEndIndex != -1 && statusEndIndex > statusStartIndex) {
            // Extract the username by removing the status
            return friendUsernameWithStatus.substring(0, statusStartIndex).trim();
        }
    }
        return null;
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
    public List<String> getFriendsOfFriends(String FriendId, String myId) {
        List<String> oldFriendFriends = getFriends(myId);
        List<String> newFriendFriends = getFriends(FriendId);
        List<String> friendsOfFriends = new ArrayList<>();
        for (String friend : oldFriendFriends) {
            if (!newFriendFriends.contains(friend) && !friend.equals(FriendId)) {
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
