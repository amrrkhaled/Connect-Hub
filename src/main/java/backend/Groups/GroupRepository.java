package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

public class GroupRepository implements IGroupRepository {

    private static final String GROUPS_FILE_PATH = "data/groups.json";
    private static final String POSTS_FILE_PATH = "data/groupsPosts.json";
    private static final String MEMBERS_FILE_PATH = "data/group_members.json";
    private static final String REQUESTS_FILE_PATH = "data/groups_join_requests.json";
    ILoadGroups loadGroups;
    IStorageHandler storageHandler;
    JSONArray groups;
    JSONArray groupsMembers;
    JSONArray requests;

    public GroupRepository(ILoadGroups loadGroups, IStorageHandler storageHandler) {
        this.loadGroups = loadGroups;
        groups = loadGroups.loadGroups();
        groupsMembers = storageHandler.loadDataAsArray(MEMBERS_FILE_PATH);
        this.storageHandler = storageHandler;
        this.requests = storageHandler.loadDataAsArray(REQUESTS_FILE_PATH);
    }

    public boolean isUserPrimaryAdmin(String userId, String groupName) {
        // Check if user is a primary admin of the group

        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.optJSONObject(i);
            if (group.optString("groupName").equals(groupName)) {
                String primaryAdminId = group.optString("primaryAdminId", "");
                if (primaryAdminId.equals(userId)) {
                    return true;  // User is the primary admin
                }
            }
        }

        // Check if user is a member of the group by passing groupsMembers
        JSONArray members = getGroupMembers(groupName, groupsMembers);
        for (int j = 0; j < members.length(); j++) {
            if (members.optString(j).equals(userId)) {
                return true;  // User is a member of the group
            }
        }

        return false;  // User is neither a member nor a primary admin
    }

    public boolean isUserGeneralAdmin(String userId, String groupName) {

        for (int i = 0; i < groups.length(); i++) {
            JSONObject group = groups.optJSONObject(i);
            if (group.optString("groupName").equals(groupName)) {
                if (!group.has("admins"))
                    return false;
                JSONArray groupAdmins = group.optJSONArray("admins");
                for (int j = 0; j < groupAdmins.length(); j++) {
                    if (groupAdmins.optString(j).equals(userId)) {
                        return true;
                    }
                }

            }
        }
        return false;  // User is neither a member nor a primary admin
    }

    public boolean isUserMember(String userId, String groupName) {

        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groups.optJSONObject(i);
            if (group.optString("groupName").equals(groupName)) {
                JSONArray groupMembers = group.optJSONArray("members");
                for (int j = 0; j < groupMembers.length(); j++) {
                    if (groupMembers.optString(j).equals(userId)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public boolean hasUserSentRequest(String userId, String groupName) {
        // Check if user has already sent a request to join the group
        for (int i = 0; i < requests.length(); i++) {
            JSONObject requestGroup = requests.optJSONObject(i);
            if (requestGroup == null) {
                continue; // Skip invalid or null request group data
            }
            if (requestGroup.optString("groupName").equals(groupName)) {
                JSONArray groupRequests = requestGroup.optJSONArray("requests");
                if (groupRequests != null) {
                    for (int j = 0; j < groupRequests.length(); j++) {
                        JSONObject request = groupRequests.optJSONObject(j);
                        if (request == null) {
                            continue; // Skip invalid or null request data
                        }
                        String requestUserId = request.optString("userId", "");
                        if (requestUserId.equals(userId)) {
                            return true;  // User has already sent a request
                        }
                    }
                }
            }
        }
        return false;  // User hasn't sent a request to join this group
    }

    public JSONArray getGroupMembers(String groupName, JSONArray groupsMembers) {
        // This method will fetch and return the members of a given group
        // Iterate through the groupsMembers to find the group by name
        for (int i = 0; i < groupsMembers.length(); i++) {
            JSONObject group = groupsMembers.optJSONObject(i);
            if (group == null) {
                continue; // Skip invalid or null group data
            }
            if (group.optString("groupName").equals(groupName)) {
                return group.optJSONArray("members");
            }
        }
        return new JSONArray();  // Return an empty array if no members are found
    }
    public JSONArray getGroupMembersByGroupName(String groupName) {
        for (int i = 0; i < groups.length(); i++) {
            System.out.println(groups.optJSONObject(i).optString("groupName"));
            System.out.println(groupName);
            JSONObject group = groups.optJSONObject(i);
            if (group.optString("groupName").equals(groupName)) {
                for (int j = 0; j < groupsMembers.length(); j++) {
                    JSONObject member = groupsMembers.optJSONObject(j);
                    if (member.optString("groupName").equals(groupName)) {
                        return member.optJSONArray("members");
                    }
                }
            }
        }
        return new JSONArray();
    }


    public JSONObject getPostById(String postId) {
        JSONArray posts = storageHandler.loadDataAsArray(POSTS_FILE_PATH);
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.optJSONObject(i);
            if (post.getString("contentId").equals(postId)) {
                return post;
            }
        }
        return null;
    }
}
