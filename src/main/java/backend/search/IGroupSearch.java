package backend.search;

import backend.Groups.Group;

import java.util.List;

public interface IGroupSearch {
    List<Group> searchGroups(String keyword);
    String joinGroup(String userId, String groupId);
    String leaveGroup(String userId, String groupId);
}
