package backend.Groups;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface ILoadGroups {
    public JSONArray loadGroups();

    public JSONArray loadGroupsByUserId(String userId);

    public JSONObject loadGroupByName(String name);

    public List<String> loadGroupSuggestions(String userId);

    public JSONArray loadPosts(String groupId);

    public JSONArray loadGroupPostsByName(String groupName);

}
