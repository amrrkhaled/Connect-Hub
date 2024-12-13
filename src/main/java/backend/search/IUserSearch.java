package backend.search;

import java.util.List;

public interface IUserSearch {
    List<String> searchUsers(String keyword);
    String addFriend(String userId, String friendId);
    String removeFriend(String userId, String friendId);
    String blockUser(String userId, String targetId);
}
