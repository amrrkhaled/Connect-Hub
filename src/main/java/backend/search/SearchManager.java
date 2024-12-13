package backend.search;

import backend.Groups.*;
import backend.user.ILoadUsers;
import backend.user.LoadUsers;
import backend.user.UserRepository;
import backend.friendship.FriendShip;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchManager implements IUserSearch, IGroupSearch {
    private final ILoadUsers loadUsers = new LoadUsers();
    IStorageHandler storageHandler = new StorageHandler();
    ILoadGroups loadGroups = LoadGroups.getInstance(storageHandler);

    // Creating instances of controllers
    GroupManager groupManager = new GroupManager(loadGroups);
    NormalUserController normalUserController = new NormalUserController(loadGroups, storageHandler);
    PrimaryAdmin primaryAdminController = new PrimaryAdmin(loadGroups, storageHandler);
    Request requestController = new Request(loadGroups, storageHandler);

    private final UserRepository userRepository;
    private final FriendShip friendShip;

    public JSONObject findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public SearchManager(GroupManager groupManager, UserRepository userRepository, FriendShip friendShip) {
        this.groupManager = groupManager;
        this.userRepository = userRepository;
        this.friendShip = friendShip;
    }

    @Override
    public List<String> searchUsers(String keyword) {
        JSONArray users = loadUsers.loadUsers();
        List<String> userList = new ArrayList<>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if(user.getString("username").toLowerCase().contains(keyword.toLowerCase())) {
                userList.add(user.getString("username"));
            }
        }
        return userList;
    }

    @Override
    public List<String> searchGroups(String keyword) {
        JSONArray groups = loadGroups.loadGroups();
        List<String> matchingGroups = new ArrayList<>();

        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.getJSONObject(i);
            if (group.getString("name").contains(keyword)) {
                matchingGroups.add(group.getString("name"));
            }
        }
        return matchingGroups;
    }


    @Override
    public String addFriend(String userId, String friendId) {
        friendShip.addFriend(userId, friendId);
        return "Friend added successfully!";
    }

    @Override
    public String removeFriend(String userId, String friendId) {
        friendShip.removeFriend(userId, friendId);
        return "Friend removed successfully!";
    }

    @Override
    public String blockUser(String userId, String targetId) {
        friendShip.BlockFriendship(userId, targetId);
        return "User blocked successfully!";
    }

    @Override
    public void joinGroup(String groupName, String userId) {
        requestController.sendJoinRequest(groupName, userId);
    }

    @Override
    public void leaveGroup(String groupName, String userId) {
        normalUserController.leaveGroup(groupName, userId);
    }
}
