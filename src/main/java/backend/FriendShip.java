package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class FriendShip {
    Validation validation;
    ILoadFriendShips loadFriendShips;
    IFriendShipValidation friendShipValidation;
    IFriendShipManager friendShipManager;
    private final String filePath = "data/friendships.json";

    public FriendShip(Validation validation, ILoadFriendShips loadFriendShips, IFriendShipValidation friendShipValidation, IFriendShipManager friendShipManager) {
        this.validation = validation;
        this.loadFriendShips = loadFriendShips;
        this.friendShipValidation = friendShipValidation;
        this.friendShipManager = friendShipManager;
    }

    public void addFriend(String userId1, String username) {
        JSONObject user = validation.findUserByUsername(username);
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
        JSONObject user = validation.findUserByUsername(username);
        boolean friendshipFound = false;
        String userId2 = user.getString("userId");
        JSONObject FriendShip = new JSONObject();
        FriendShip.put("userId1", userId1);
        FriendShip.put("userId2", userId2);
        FriendShip.put("status", "accepted");
        JSONArray friendships = loadFriendShips.loadFriendships();
        if (friendShipValidation.checkDuplicates(userId1, userId2, friendships)) {
            friendshipFound = friendShipManager.RemoveFriendShip(userId1, userId2, friendships);
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
        JSONObject user = validation.findUserByUsername(username);
        String userId2 = user.getString("userId");
        JSONArray friendships = loadFriendShips.loadFriendships();
        boolean friendshipFound = friendShipManager.RemoveFriendShip(userId1, userId2, friendships);
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
        JSONObject user = validation.findUserByUsername(username);
        String userId2 = user.getString("userId");
        JSONObject friendship = null;
        JSONArray friendships = loadFriendShips.loadFriendships();
        boolean friendshipFound = friendShipValidation.checkDuplicates(userId1, userId2, friendships);
        if (friendshipFound) {
            friendship = friendShipManager.FindFriendShip(userId1, userId2, friendships);
            friendShipManager.RemoveFriendShip(userId1, userId2, friendships);
        }
        friendship.put("userId1", userId1);
        friendship.put("userId2", userId2);
        friendship.put("status", "blocked");
        friendships.put(friendship);
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(friendships.toString(4));
            System.out.println("Friendship successfully blocked.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());

        }

    }
}
