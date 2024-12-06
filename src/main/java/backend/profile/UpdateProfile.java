package backend.profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class UpdateProfile implements IUpdateProfile {
    private static UpdateProfile instance;
    private UpdateProfile() {

    }
    public static synchronized UpdateProfile getInstance() {
        if (instance == null) {
            instance = new UpdateProfile();
        }
        return instance;
    }
    public void updateProfile(String userId, JSONArray profilesArray, JSONObject profile) {
       boolean found = false;
        for (int i = 0; i < profilesArray.length(); i++) {
            JSONObject existingUser = profilesArray.getJSONObject(i);
            if (existingUser.getString("userId").equals(userId)) {
                profilesArray.put(i, profile);
                found=true;
                break;
            }
        }
        if(!found){
        profilesArray.put(profile);
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
