package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IUpdateProfile {
    public void updateProfile(String userId, JSONArray profilesArray, JSONObject profile);
    public void saveProfiles(JSONArray profilesArray);
}
