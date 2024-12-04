package backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class AddProfile implements IAddProfile{
    private int profilesNumber = 1;
    private final String filePath = "data/profiles.json";
    private  ILoadProfiles loadProfiles;
    public AddProfile(ILoadProfiles loadProfiles) {

        this.loadProfiles = loadProfiles;
        JSONArray usersArray = loadProfiles.loadProfiles();
        this.profilesNumber = usersArray.length() + 1;
    }
    public void addProfile(String ProfilePicture, String CoverPhoto, String Bio , String userId){
            JSONObject newUser = new JSONObject();
            newUser.put("userId", userId);

            newUser.put("ProfilePicture", ProfilePicture);
            newUser.put("CoverPhoto",CoverPhoto);
            newUser.put("Bio", Bio);
            JSONArray usersArray = loadProfiles.loadProfiles();
            usersArray.put(newUser);
            // Write the updated array back to the file
            try (FileWriter file = new FileWriter(filePath)) {
                file.write(usersArray.toString(4));
                System.out.println("Successfully written to the file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

