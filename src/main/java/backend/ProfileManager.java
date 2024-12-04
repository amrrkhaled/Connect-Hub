package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class ProfileManager {
    IAddProfile ProfileAdder;
    ILoadProfiles ProfileLoader;
    Validation validation;
    String userId;
    IUpdateProfile ProfileUpdater;

    public ProfileManager(IAddProfile addProfile, ILoadProfiles loadProfiles, String userId, Validation validation, IUpdateProfile updateProfile) {
        this.ProfileAdder = addProfile;
        this.ProfileLoader = loadProfiles;
        this.validation = validation;
        this.userId = userId;
        this.ProfileUpdater = updateProfile;
    }

    public void add(String ProfilePicture, String CoverPhoto, String Bio) throws IOException {
        ProfileAdder.addProfile(ProfilePicture, CoverPhoto, Bio, userId);
        updateProfilePhoto(ProfilePicture);
        updateCoverPhoto(CoverPhoto);
    }

    public void updateProfilePhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = validation.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("ProfilePicture", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }

    public void updateCoverPhoto(String absoluteFilePath) throws IOException {
        JSONObject profile = validation.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        String savedImagePath = SaveImage.saveImageToFolder(absoluteFilePath);
        profile.put("CoverPhoto", savedImagePath);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }
    public void updateBio(String Bio){
        JSONObject profile = validation.findProfileByUserId(userId);
        JSONArray profiles = ProfileLoader.loadProfiles();
        profile.put("Bio", Bio);
        ProfileUpdater.updateProfile(userId, profiles, profile);
        ProfileUpdater.saveProfiles(profiles);
    }

}


