package backend.user;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public interface IPasswordUtils {
    public void updatePasswordHashForUser(String userId, String newPassword);
    public static String getStoredPasswordHashForUser(String username, JSONArray usersArray) {
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).getString("username").equals(username)) {
                return usersArray.getJSONObject(i).getString("password");
            }
        }
        return null;
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

}
