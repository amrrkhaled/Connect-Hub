package backend.search;

import backend.user.User;

import java.util.List;

public interface IUserSearch {
    List<User> searchUsers(String keyword);
    String addFriend(String userId, String friendId);
    String removeFriend(String userId, String friendId);
    String blockUser(String userId, String targetId);
}
