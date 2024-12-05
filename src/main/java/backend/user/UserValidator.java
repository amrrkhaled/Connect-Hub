package backend.user;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import backend.user.*;

public class UserValidator implements Validation {
    private final ILoadUsers loadUsers;

    public UserValidator(ILoadUsers loadUsers) {
        this.loadUsers = loadUsers;
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

    public boolean isPasswordValid(String password, String storedPasswordHash) {
        return PasswordUtils.hashPassword(password).equals(storedPasswordHash);
    }



}

