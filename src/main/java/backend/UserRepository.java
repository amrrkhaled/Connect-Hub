package backend;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserRepository implements IUserRepository {
    ILoadUsers loadUsers;

    public UserRepository(ILoadUsers loadUsers){
        this.loadUsers = loadUsers;
    }

    private JSONArray getUsersArray() {
        return loadUsers.loadUsers();
    }

    public String getUsernameByUserId(String userId) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("userId").equals(userId)) {
                    return user.getString("username");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }

    public String getStatusByUserId(String userId) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("userId").equals(userId)) {
                    return user.getString("status");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }

    public JSONObject findUserByUsername(String username) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("username").equals(username)) {
                    return user;
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }
    public String findUserIdByUsername(String username) {
        JSONArray usersArray = getUsersArray();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            try {
                if (user.getString("username").equals(username)) {
                    return user.getString("userId");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving data for user at index " + i + ": " + e.getMessage());
            }
        }
        return null;
    }
}
