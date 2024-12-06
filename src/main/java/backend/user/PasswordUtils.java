package backend.user;

import org.json.JSONArray;
import org.json.JSONObject;

public class PasswordUtils implements IPasswordUtils {
    ILoadUsers loadUsers;
    IUpdateUser updateUser;
    private static PasswordUtils instance;
   private PasswordUtils(ILoadUsers loadUsers , IUpdateUser updateUser) {
       this.loadUsers = loadUsers;
       this.updateUser = updateUser;
   }
    public static synchronized PasswordUtils getInstance(ILoadUsers loadUsers , IUpdateUser updateUser) {
        if (instance == null) {
            instance = new PasswordUtils(loadUsers , updateUser);
        }
        return instance;
    }
    public void updatePasswordHashForUser(String userId, String newPassword) {
        JSONArray usersArray =loadUsers.loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).getString("userId").equals(userId) &&usersArray.getJSONObject(i).getString("status").equals("online")) {
                JSONObject user = usersArray.getJSONObject(i);
                newPassword= IPasswordUtils.hashPassword(newPassword);
                user.put("password", newPassword);
                updateUser.updateUser(userId , usersArray , user);
                updateUser.saveUsers(usersArray);
                break;
            }
        }
    }
}

