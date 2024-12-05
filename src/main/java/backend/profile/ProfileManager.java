package backend.profile;

import backend.SaveImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class ProfileManager {
    ILoadProfiles ProfileLoader;
    String userId;
    IUpdateProfile ProfileUpdater;

    public ProfileManager(ILoadProfiles loadProfiles, String userId, IUpdateProfile updateProfile) {
        this.ProfileLoader = loadProfiles;
        this.userId = userId;
        this.ProfileUpdater = updateProfile;
    }
    public void updateProfilePhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("ProfilePicture", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }

    public void updateCoverPhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("CoverPhoto", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }
    public void updateBio(String Bio){
        JSONObject profile = findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        profile.put("Bio", Bio);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }
    public JSONObject findProfileByUserId(String userId) {
        JSONArray profiles = ProfileLoader.loadProfiles();
        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            if (profile.getString("userId").equals(userId)) {
                return profile;
            }
        }
        return null;
    }

}


