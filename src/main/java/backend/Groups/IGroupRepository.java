package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IGroupRepository {
    public boolean hasUserSentRequest(String userId, String groupName);

    public JSONArray getGroupMembers(String groupName, JSONArray groupsMembers);
    public JSONArray getGroupMembersByGroupName(String groupName);
    public JSONObject getPostById(String postId);

    public boolean isUserGeneralAdmin(String userId, String groupName);

    public boolean isUserPrimaryAdmin(String userId, String groupName);

    public boolean isUserMember(String userId, String groupName);
}
