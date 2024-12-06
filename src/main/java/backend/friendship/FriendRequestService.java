package backend.friendship;

import backend.user.ILoadUsers;
import backend.user.IUserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendRequestService implements IFriendRequestService {
    private IUserRepository userRepository;
    private IFriendshipService friendshipService;
    private ILoadFriendShips loadFriendShips;
    private ILoadUsers loadUsers;
    public FriendRequestService(IUserRepository userRepository, IFriendshipService friendshipService, ILoadFriendShips loadFriendShips, ILoadUsers loadUsers) {
        this.userRepository = userRepository;
        this.friendshipService = friendshipService;
        this.loadFriendShips = loadFriendShips;
        this.loadUsers = loadUsers;
    }

    public IFriendshipService getFriendshipService() {
        return friendshipService;
    }

    public ILoadUsers getLoadUsers() {
        return loadUsers;
    }
    public ILoadFriendShips getLoadFriendShips() {
        return loadFriendShips;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public String extractUsername(String friendUsernameWithStatus) {
        if (friendUsernameWithStatus != null) {
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
    // return friend suggestions
    public List<String> getFriendSuggestions(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        Set<String> friendSuggestions = new HashSet<>(); // Use Set to avoid duplicates
        List<String> userFriends = friendshipService.getFriends(userId);
        if (userFriends.size() > 0) {
            for (String friend : userFriends) {
                // Find mutual friends who are not already direct friends
                List<String> friendsOfFriends = friendshipService.getFriendsOfFriends(friend, userId);
                friendSuggestions.addAll(friendsOfFriends);
            }
        } else {
            List<String> pendingFriends = friendshipService.getPendingFriends(userId);
            List<String> usernames = friendshipService.extractUsernames(pendingFriends);
            List<String> friendRequests = getFriendRequests(userId);
            List<String> BlockedFriends = friendshipService.getBlockedFriends(userId);
            System.out.println("Pending friends: :");
            for (String username : usernames) {
                System.out.println(username);
            }
            System.out.println("FriendsUsernames :");
            for (int i = 0; i < userFriends.size(); i++) {
                String friendId = userFriends.get(i);
                String username = userRepository.getUsernameByUserId(friendId);
                System.out.println(username);
                userFriends.set(i, username);
            }
            System.out.println("FriendRequests :");
            for (int i = 0; i < friendRequests.size(); i++) {
                String usernameFriendRequest = friendRequests.get(i);
                System.out.println(usernameFriendRequest);
            }
           System.out.println("BlockedFriends :");
            for (int i = 0; i < BlockedFriends.size(); i++) {
                String friendId = BlockedFriends.get(i);
                String username = userRepository.getUsernameByUserId(friendId);
                System.out.println(username);
                BlockedFriends.set(i, username);
            }
            JSONArray users = loadUsers.loadUsers();
            for (int i = 0; i < users.length(); i++) {
                JSONObject userJson = users.getJSONObject(i);
                String suggestion = userJson.getString("username");
                String myself = userRepository.getUsernameByUserId(userId);
                System.out.println("suggestion :");
                System.out.println(suggestion);
                if (!userFriends.contains(suggestion)&&!(usernames.contains(suggestion)) && !friendRequests.contains(suggestion) && !(myself.equals(suggestion))&& !BlockedFriends.contains(suggestion)) {
                    friendSuggestions.add(suggestion);
//                    System.out.println(suggestion);
//                    System.out.println(usernames);
                }
            }
        }
        return new ArrayList<>(friendSuggestions);
    }




    // return usernames of friend requests.
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

}
