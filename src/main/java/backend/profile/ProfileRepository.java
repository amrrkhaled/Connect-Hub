package backend.profile;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileRepository implements IProfileRepository {
    private ILoadProfiles loadProfiles;
    private static ProfileRepository instance;
    private ProfileRepository(ILoadProfiles loadProfiles) {
        this.loadProfiles = loadProfiles;
    }
    public static ProfileRepository getInstance(ILoadProfiles loadProfiles) {
        if (instance == null) {
            instance = new ProfileRepository(loadProfiles);
        }
        return instance;
    }
    public JSONObject findProfileByUserId(String userId) {
        JSONArray profiles = loadProfiles.loadProfiles();
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            if (profile.getString("userId").equals(userId)) {
                return profile;
            }
        }
        return null;
    }

}
