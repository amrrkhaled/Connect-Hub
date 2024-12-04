package backend;

import org.json.JSONArray;
import org.json.JSONObject;


public class UserValidator implements Validation {
    private final ILoadUsers loadUsers;
    private final ILoadProfiles loadProfiles;
    public UserValidator(ILoadUsers loadUsers , ILoadProfiles loadProfiles) {

        this.loadUsers = loadUsers;
        this.loadProfiles = loadProfiles;
    }

    public boolean doesUsernameExist(String username) {
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean doesEmailExist(String email) {
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                return true;
            }
        }
        return false;
    }

    public JSONObject findUserByUsername(String username) {
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("username").equals(username)) {
                return user;
            }
        }
        return null;
    }
    public String findUsernameByUserId(String userId) {
        JSONArray usersArray = loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("userId").equals(userId)) {
                return user.getString("username");
            }
        }
        return null;
    }

    public boolean isPasswordValid(String password, String storedPasswordHash) {
        return PasswordUtils.hashPassword(password).equals(storedPasswordHash);
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

