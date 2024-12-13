package backend.search;

import backend.Groups.Group;

import java.util.List;

public interface IGroupSearch {
    List<String> searchGroups(String keyword);
    void joinGroup(String userId, String groupId);
    void leaveGroup(String userId, String groupId);
}
