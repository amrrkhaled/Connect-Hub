package backend.Groups;


import org.json.JSONArray;

import java.util.List;

public class GroupManagementFacade {
    private GeneralAdmin generalAdmin;
    private PrimaryAdmin primaryAdmin;
    private NormalUser normalUser;

    public GroupManagementFacade(ILoadGroups loadGroups, IStorageHandler storageHandler) {

        this.generalAdmin = new GeneralAdmin(loadGroups, storageHandler);
        this.primaryAdmin = new PrimaryAdmin(loadGroups, storageHandler);
        this.normalUser = new NormalUser(loadGroups, storageHandler);
    }

    public void handleMemberJoinRequest(String groupName, String userId, boolean accept) {
        if (accept) {
            generalAdmin.acceptMember(groupName, userId);
        } else {
            generalAdmin.rejectMember(groupName, userId);
        }
    }

    public void removeGroupMember(String groupName, String userId) {
        generalAdmin.removeMember(groupName, userId);
    }

    public void addPost(String groupName, String authorId, String content, String timestamp, List<String> images) {
        normalUser.addPost(groupName, authorId, content, timestamp, images);
    }

    public void deletePost(String postId) {
        generalAdmin.deletePost(postId);
    }

    public void userLeaveGroup(String groupName, String userId) {
        normalUser.leaveGroup(groupName, userId);
    }

    public List<String> getUserGroups(String userId) {
        return normalUser.getGroupsForUser(userId);
    }

    public void addAdminToGroup(String groupName, String userId) {
        primaryAdmin.addAdminToGroup(groupName, userId);
    }

    public void updateGroupDescription(String groupName, String description) {
        primaryAdmin.updateDescription(groupName, description);
    }

    public void addCommentToPost(String postId, String comment) {
        normalUser.addComment(postId, comment);
    }

    public JSONArray getCommentsByPost(String postId) {
        return normalUser.getCommentsByPost(postId);
    }

    public void removeAdminFromGroup(String groupName, String userId) {
        primaryAdmin.removeAdmin(groupName, userId);
    }

    public void deleteGroup(String groupName) {
        primaryAdmin.deleteGroup(groupName);
    }
}
