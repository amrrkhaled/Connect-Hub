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
    // return username(state)
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
    // return usernames of friends of friends.
    public List<String> getFriendsOfFriends(String FriendId, String myId) {
        List<String> myFriends = getFriends(myId);
        List<String> FriendsOfmyFriend = getFriends(FriendId);
        List<String> friendsOfFriends = new ArrayList<>();
        List<String> pendingFriends = getPendingFriends(myId);
        List<String> usernames = extractUsernames(pendingFriends);
        IFriendRequestService friendRequestService = FriendRequestServiceFactory.getInstance().createFriendRequestService();
        List<String> friendRequests = friendRequestService.getFriendRequests(myId);
        List<String> BlockedFriends = getBlockedFriends(myId);
        System.out.println("Pending friends: :");
        for (int i = 0; i < myFriends.size(); i++) {
            String friendId = FriendsOfmyFriend.get(i);
            String username = userRepository.getUsernameByUserId(friendId);
            System.out.println(username);
            FriendsOfmyFriend.set(i, username);
        }
        for (String username : usernames) {
            System.out.println(username);
        }
        System.out.println("FriendsUsernames :");
        for (int i = 0; i < myFriends.size(); i++) {
            String friendId = myFriends.get(i);
            String username = userRepository.getUsernameByUserId(friendId);
            System.out.println(username);
            myFriends.set(i, username);
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

//        for (String username : usernames) {
//            usernames.
//        }

        for (String friend : FriendsOfmyFriend) {
            if (!myFriends.contains(friend) && !friend.equals(myId) &&!friend.equals(FriendId) && !usernames.contains(friend)&& !BlockedFriends.contains(friend) && !friendRequests.contains(friend)) {
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
    // return usenames of pendingFriends
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
    // return ids of blocked friends
    public List<String> getBlockedFriends(String userId) {
        JSONArray friendships = loadFriendShips.loadFriendships();
        List<String> friends = new ArrayList<>();

        for (int i = 0; i < friendships.length(); i++) {
            JSONObject friendship = friendships.getJSONObject(i);

            if (friendship.getString("status").equals("blocked")) {
                if (friendship.getString("userId1").equals(userId)) {
                    friends.add(friendship.getString("userId2"));
                } else if (friendship.getString("userId2").equals(userId)) {
                    friends.add(friendship.getString("userId1"));
                }
            }
        }

        return friends;
    }
    //return ids of friends
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
    // return usernames(status)
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
    // remove friendShip
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
   // find friendShip
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
