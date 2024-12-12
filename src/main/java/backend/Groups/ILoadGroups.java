package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ILoadGroups {
    public JSONArray loadGroups();
    public JSONArray loadGroupsbyUserId(String userId);
    public JSONObject loadGroupByName(String name);
    public JSONArray loadPosts(String groupId);
}
