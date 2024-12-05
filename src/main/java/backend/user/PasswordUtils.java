package backend.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

public class PasswordUtils{
    ILoadUsers loadUsers;
    IUpdateUser updateUser;
    public PasswordUtils(ILoadUsers loadUsers , IUpdateUser updateUser) {
        this.loadUsers = loadUsers;
        this.updateUser = updateUser;
    }
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getStoredPasswordHashForUser(String username, JSONArray usersArray) {
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).getString("username").equals(username)) {
                return usersArray.getJSONObject(i).getString("password");
            }
        }
        return null;
    }

    public void updatePasswordHashForUser(String userId, String newPassword) {
        JSONArray usersArray =loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).getString("userId").equals(userId) &&usersArray.getJSONObject(i).getString("status").equals("online")) {
                JSONObject user = usersArray.getJSONObject(i);
                newPassword=hashPassword(newPassword);
                user.put("password", newPassword);
                updateUser.updateUser(userId , usersArray , user);
                updateUser.saveUsers(usersArray);
                break;
            }
        }
    }
}
