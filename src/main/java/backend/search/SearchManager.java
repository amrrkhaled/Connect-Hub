package backend.search;

import backend.user.User;
import backend.Groups.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchManager implements IUserSearch, IGroupSearch {
    Object Database = null;
    @Override
    public List<User> searchUsers(String keyword) {
        return Database.getUsers().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> searchGroups(String keyword) {
        return Database.getGroups().stream()
                .filter(group -> group.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public String addFriend(String userId, String friendId) {
        return Database.addFriend(userId, friendId);
    }

    @Override
    public String removeFriend(String userId, String friendId) {
        return Database.removeFriend(userId, friendId);
    }

    @Override
    public String blockUser(String userId, String targetId) {
        return Database.blockUser(userId, targetId);
    }

    @Override
    public String joinGroup(String userId, String groupId) {
        return Database.joinGroup(userId, groupId);
    }

    @Override
    public String leaveGroup(String userId, String groupId) {
        return Database.leaveGroup(userId, groupId);
    }
}
