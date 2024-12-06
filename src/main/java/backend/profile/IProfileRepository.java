package backend.profile;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IProfileRepository {
    public JSONObject findProfileByUserId(String userId);
}
