package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class UpdateProfile implements IUpdateProfile {
    public void updateProfile(String userId, JSONArray profilesArray, JSONObject profile) {
        for (int i = 0; i < profilesArray.length(); i++) {
            JSONObject existingUser = profilesArray.getJSONObject(i);
            if (existingUser.getString("userId").equals(userId)) {
                profilesArray.put(i, profile);
                break;
            }
        }
    }

    public void saveProfiles(JSONArray profilesArray) {
        try (FileWriter file = new FileWriter("data/profiles.json")) {
            file.write(profilesArray.toString(4)); // Indented for readability
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
