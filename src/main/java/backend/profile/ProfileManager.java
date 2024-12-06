package backend.profile;

import backend.SaveImage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class ProfileManager {
    ILoadProfiles ProfileLoader;
    String userId;
    IUpdateProfile ProfileUpdater;
    IProfileRepository repo;
    public ProfileManager(ILoadProfiles loadProfiles, String userId, IUpdateProfile updateProfile,IProfileRepository repo) {
        this.ProfileLoader = loadProfiles;
        this.userId = userId;
        this.ProfileUpdater = updateProfile;
        this.repo = repo;
    }
    public void updateProfilePhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = repo.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("ProfilePicture", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }

    public void updateCoverPhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = repo.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("CoverPhoto", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }
    public void updateBio(String Bio){
        JSONObject profile = repo.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        profile.put("Bio", Bio);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }
    public void intiallizeProfile(){

        JSONArray profiles = ProfileLoader.loadProfiles();
        JSONObject profileJson = new JSONObject();
        profileJson.put("userId", userId);
        profileJson.put("Bio", "");
        profileJson.put("ProfilePicture","images/defualtPp.jpg");
        profiles.put(profileJson);
        ProfileUpdater.saveProfiles(profiles);

    }

    public String getUserId() {
        return userId;
    }
    public IProfileRepository getRepo() {
        return repo;
    }
    public ILoadProfiles getProfileLoader() {
        return ProfileLoader;
    }
    public IUpdateProfile getProfileUpdater() {
        return ProfileUpdater;
    }

}


