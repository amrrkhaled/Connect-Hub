package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GeneralAdminController extends NormalUserController {
    private final String membersFilePath = "data/group_members.json";
    private final String joinRequestsFilePath = "data/groups_join_requests.json";
    private final String postsFilePath = "data/groupsPosts.json";

    public GeneralAdminController(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        super(loadGroups, storageHandler);
    }

    public void acceptMember(String groupName, String userId) {
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);
        JSONArray joinRequestsData = storageHandler.loadDataAsArray(joinRequestsFilePath);

        JSONObject groupReq = null;
        for (int i = 0; i < joinRequestsData.length(); i++) {
            JSONObject groupObject = joinRequestsData.getJSONObject(i);
            if (groupObject.getString("groupName").equals(groupName)) {
                groupReq = groupObject;
                break;
            }
        }

        if (groupReq == null) {
            System.out.println("Group with name '" + groupName + "' not found.");
            return;
        }

        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            if (groupMemberObject.getString("groupName").equals(groupName)) {
                JSONArray members = groupMemberObject.getJSONArray("members");
                for (int j = 0; j < members.length(); j++) {
                    if (members.optString(j).equals(userId)) {
                        System.out.println("User " + userId + " is already a member of group: " + groupName);
                        return;
                    }
                }
            }
        }

        JSONArray requests = groupReq.getJSONArray("requests");
        boolean userFound = false;
        for (int i = 0; i < requests.length(); i++) {
            JSONObject requestObject = requests.getJSONObject(i);
            if (requestObject.getString("userId").equals(userId)) {
                requests.remove(i);
                userFound = true;
                break;
            }
        }
        if (!userFound) {
            System.out.println("User " + userId + " not found in join requests for group: " + groupName);
            return;
        }
        boolean groupFound = false;
        for (int j = 0; j < groupsMembers.length(); j++) {
            JSONObject groupMember = groupsMembers.getJSONObject(j);
            if (groupMember.getString("groupName").equals(groupName)) {
                groupMember.getJSONArray("members").put(userId);
                groupFound = true;
                break;
            }
        }

        if (!groupFound) {
            JSONObject newGroupMember = new JSONObject();
            newGroupMember.put("groupName", groupName);
            newGroupMember.put("members", new JSONArray().put(userId));
            groupsMembers.put(newGroupMember);
        }
        storageHandler.saveDataAsArray(joinRequestsData, joinRequestsFilePath);
        storageHandler.saveDataAsArray(groupsMembers, membersFilePath);
        System.out.println("User " + userId + " added to group: " + groupName);
    }
    public void rejectMember(String groupName, String userId) {
        JSONArray joinRequestsData = storageHandler.loadDataAsArray(joinRequestsFilePath);

        for (int i = 0; i < joinRequestsData.length(); i++) {
            JSONObject joinRequestObject = joinRequestsData.getJSONObject(i);
            if (joinRequestObject.getString("groupName").equals(groupName)) {
                JSONArray requests = joinRequestObject.getJSONArray("requests");
                for (int j = 0; j < requests.length(); j++) {
                    if (requests.getJSONObject(j).getString("userId").equals(userId)) {
                        requests.remove(j);
                        storageHandler.saveDataAsArray(joinRequestsData, joinRequestsFilePath);
                        System.out.println("User " + userId + " rejected from group: " + groupName);
                        return;
                    }
                }
            }
        }
        System.out.println("User " + userId + " not found in join requests for group: " + groupName);
    }

    public void removeMember(String groupName, String userId) {
        JSONArray groupsMembers = storageHandler.loadDataAsArray(membersFilePath);

        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject groupMemberObject = groupsMembers.getJSONObject(i);
            if (groupMemberObject.getString("groupName").equals(groupName)) {
                JSONArray members = groupMemberObject.getJSONArray("members");
                for (int j = 0; j < members.length(); j++) {
                    if (members.optString(j).equals(userId)) {
                        members.remove(j);
                        storageHandler.saveDataAsArray(groupsMembers, membersFilePath);
                        System.out.println("User " + userId + " removed from group: " + groupName);
                        return;
                    }
                }
            }
        }
        System.out.println("User " + userId + " not found in group: " + groupName);
    }

    public void editPostImages(String postId, List<String> images) {
        JSONArray posts = storageHandler.loadDataAsArray(postsFilePath);
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            if (post.getString("contentId").equals(postId)) {
                post.put("images", images);
                posts.put(i, post);
                break;
            }
        }
        storageHandler.saveDataAsArray(posts, postsFilePath);
    }

    public void editPostContent(String postId, String content) {
        JSONArray posts = storageHandler.loadDataAsArray(postsFilePath);
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            if (post.getString("contentId").equals(postId)) {
                post.put("content", content);
                posts.put(i, post);
                break;
            }
        }
        storageHandler.saveDataAsArray(posts, postsFilePath);
    }

    public void deletePost(String postId) {
        JSONArray posts = storageHandler.loadDataAsArray(postsFilePath);

        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            if (post.getString("contentId").equals(postId)) {
                System.out.println("Post " + postId + " deleted");

                posts.remove(i);
                break;
            }
        }
        storageHandler.saveDataAsArray(posts, postsFilePath);
    }
}
