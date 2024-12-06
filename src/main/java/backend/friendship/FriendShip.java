package backend.friendship;

import backend.user.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class FriendShip {
    Validation validation;
    ILoadFriendShips loadFriendShips;
    IFriendShipValidation friendShipValidation;
    IUserRepository userRepository;
    IFriendshipService friendshipService;
    IFriendRequestService friendRequestService;
    ILoadUsers loadUsers;
    private final String filePath = "data/friendships.json";

    public FriendShip(IUserRepository userRepository, ILoadFriendShips loadFriendShips,IFriendShipValidation friendShipValidation,IFriendshipService friendshipService,IFriendRequestService friendRequestService,ILoadUsers loadUsers) {
        this.loadFriendShips = loadFriendShips;
        this.friendShipValidation = friendShipValidation;
        this.userRepository = userRepository;
        this.loadUsers = loadUsers;
        this.friendRequestService = friendRequestService;
        this.friendshipService = friendshipService;

    }


    public IFriendshipService getFriendshipService() {
        return friendshipService;
    }
    public IFriendRequestService getFriendRequestService() {
        return friendRequestService;
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance(loadUsers);
    }
    public LoadUsers getLoadUsers() {
        return LoadUsers.getInstance();
    }
    public ILoadFriendShips getLoadFriendShips() {
        return loadFriendShips;
    }
    public IFriendShipValidation getFriendShipValidation() {
        return friendShipValidation;
    }
    public void addFriend(String userId1, String username) {
        JSONObject user = userRepository.findUserByUsername(username);
        String userId2 = user.getString("userId");
        JSONObject FriendShip = new JSONObject();
        FriendShip.put("userId1", userId1);
        FriendShip.put("userId2", userId2);
        FriendShip.put("status", "pending");
        JSONArray friendships = loadFriendShips.loadFriendships();
        if (friendShipValidation.checkDuplicates(userId1, userId2, friendships)) {
            return;
        }
        friendships.put(FriendShip);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(friendships.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void acceptFriend(String userId1, String username) {
        JSONObject user = userRepository.findUserByUsername(username);
        boolean friendshipFound = false;
        String userId2 = user.getString("userId");
        JSONObject FriendShip = new JSONObject();
        FriendShip.put("userId1", userId2);
        FriendShip.put("userId2", userId1);
        FriendShip.put("status", "accepted");
        JSONArray friendships = loadFriendShips.loadFriendships();
        if (friendShipValidation.checkDuplicates(userId1, userId2, friendships)) {
            friendshipFound =friendshipService.RemoveFriendShip(userId1, userId2, friendships);
        }
        friendships.put(FriendShip);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(friendships.toString(4));
            System.out.println("Successfully written to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFriend(String userId1, String username) {
        JSONObject user = userRepository.findUserByUsername(username);
        System.out.println(username);
        String userId2 = user.getString("userId");
        JSONArray friendships = loadFriendShips.loadFriendships();
        boolean friendshipFound = friendshipService.RemoveFriendShip(userId1, userId2, friendships);
        if (friendshipFound) {
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(friendships.toString(4));
                System.out.println("Friendship successfully removed.");
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.println("Friendship not found.");
        }
    }

    public void BlockFriendship(String userId1, String username) {
        JSONObject user = userRepository.findUserByUsername(username);
        String userId2="";
        if(user != null) {
             userId2 = user.getString("userId");
        }else{
            System.out.println("User not found!");
            return;
        }
        JSONObject friendship = null;
        JSONArray friendships = loadFriendShips.loadFriendships();
        boolean friendshipFound = friendShipValidation.checkDuplicates(userId1, userId2, friendships);
        if (friendshipFound) {
            friendship = friendshipService.FindFriendShip(userId1, userId2, friendships);
            if(friendship != null) {
                friendshipService.RemoveFriendShip(userId1, userId2, friendships);
                friendship.put("userId1", userId1);
                friendship.put("userId2", userId2);
                friendship.put("status", "blocked");
                friendships.put(friendship);
            }else{
                System.out.println("Friendship not found.");
                return;
            }

        }else{
            System.out.println("No existing friendship found.");
            return; // Exit if no friendship exists
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(friendships.toString(4));
            System.out.println("Friendship successfully blocked.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());

        }

    }
}
